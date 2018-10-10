package io.yetanotherwhatever.ocpv2.aws;

import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import io.yetanotherwhatever.ocpv2.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Created by achang on 9/3/2018.
 */
public class DynamoOcpV2DB implements IOcpV2DB {

    static final Logger logger = LogManager.getLogger(DynamoOcpV2DB.class);


    private static final String REGISTRATION_TABLE_NAME = System.getenv("DYNAMODB_REGISTRATION_TABLE");
    private static final String OUTPUT_UPLOAD_TABLE= System.getenv("DYNAMODB_OUTPUT_UPLOADS_TABLE");

    //Registtration table attributes
    private static final String R_FIRST = "First";
    private static final String R_LAST = "Last";
    private static final String R_EMAIL = "Email";
    private static final String R_MGR_EMAIL = "ManagerEmail";
    private static final String R_DATE = "CreatedDate";
    private static final String R_PROBLEM_KEY = "ProblemKey";
    private static final String R_PROBLEM_LANDING_PAGE= "LandingPageURL";
    private static final String R_PROBLEM_GUID= "ProblemPageGuid";    //table key
    private static final String R_SUCCEEDED = "Succeeded";
    private static final String R_ATTEMPTS = "Attempts";

    //Output upload table attributes
    private static final String O_UPLOAD_ID = "UploadId";
    private static final String O_INVITATION_ID = R_PROBLEM_GUID;
    private static final String O_RESULT = "Result";
    private static final String O_OUTPUT_UPLOAD_DATE = "UploadDate";

    private static AmazonDynamoDB addb;

    public DynamoOcpV2DB ()
    {

    }

    private AmazonDynamoDB getAmazonDynamoDB()
    {
        //lazy load
        if (null == addb)
        {
            addb = AmazonDynamoDBClientBuilder.defaultClient();
        }

        return addb;
    }

    @Override
    public void write(CandidateRegistration cr) throws IOException {

        HashMap<String,AttributeValue> item_values =
                new HashMap<>();

        Invitation i = cr.getInvitation();
        item_values.put(R_FIRST, new AttributeValue(i.getCandidateFirstName()));
        item_values.put(R_LAST, new AttributeValue(i.getCandidateLastName()));
        item_values.put(R_EMAIL, new AttributeValue(i.getCandidateEmail()));
        item_values.put(R_MGR_EMAIL, new AttributeValue(i.getManagerEmail()));
        item_values.put(R_DATE, new AttributeValue(i.getInvitationDate()));

        CodingProblem cp = cr.getCodingProblem();
        item_values.put(R_PROBLEM_GUID, new AttributeValue(cp.getGuid()));
        item_values.put(R_PROBLEM_KEY, new AttributeValue(cp.getName()));
        item_values.put(R_PROBLEM_LANDING_PAGE, new AttributeValue(cp.getLandingPageUrl()));

        item_values.put(R_SUCCEEDED, new AttributeValue("Never"));
        item_values.put(R_ATTEMPTS, new AttributeValue().withN("0"));


        try {
            getAmazonDynamoDB().putItem(REGISTRATION_TABLE_NAME, item_values);

            logger.info("Registration successfully saved to DynamoDB table " + REGISTRATION_TABLE_NAME);

            for (String key : item_values.keySet())
            {
                String val = item_values.get(key).getS();
                logger.info(key + " : " + val);
            }

        } catch (ResourceNotFoundException e) {
            throw new IOException(e);
        } catch (AmazonServiceException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void updateRegistration(String registrationId, String outputUploadDate, boolean success) throws IOException
    {

        try {
            DynamoDB ddb = new DynamoDB(getAmazonDynamoDB());

            Table table = ddb.getTable(REGISTRATION_TABLE_NAME);

            HashMap<String, String> expressionAttributeNames = new HashMap<String, String>();
            expressionAttributeNames.put("#A", R_ATTEMPTS);

            HashMap<String, Object> expressionAttributeValues = new HashMap<String, Object>();
            expressionAttributeValues.put(":val1", 1);

            //only update succeeded date if success
            String updateExpression;
            if (success)
            {
                expressionAttributeNames.put("#S", R_SUCCEEDED);
                expressionAttributeValues.put(":val2", outputUploadDate);
                updateExpression = "set #A = #A + :val1, #S = :val2 "; // set last update time, increment attempts
            }
            //else just update attempts
            else
            {
                updateExpression = "set #A = #A + :val1"; // set last update time, increment attempts
            }

            logger.debug(updateExpression);

            table.updateItem(
                    R_PROBLEM_GUID, // key attribute name
                    registrationId,   // key attribute value
                    updateExpression,
                    expressionAttributeNames,
                    expressionAttributeValues);

            logger.debug("Invitation update succeeded for record: " + registrationId);

        } catch (ResourceNotFoundException e) {
            throw new IOException(e);
        } catch (AmazonServiceException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void write(OutputResults or) throws IOException {

        HashMap<String,AttributeValue> item_values =
                new HashMap<>();

        item_values.put(O_UPLOAD_ID, new AttributeValue(or.getUploadID()));
        item_values.put(O_INVITATION_ID, new AttributeValue(or.getInvitationId()));
        item_values.put(O_RESULT, new AttributeValue(or.getResults()));
        item_values.put(O_OUTPUT_UPLOAD_DATE, new AttributeValue(or.getUploadDate()));


        try {
            getAmazonDynamoDB().putItem(OUTPUT_UPLOAD_TABLE, item_values);

            logger.info("Output upload result successfully saved to DynamoDB table " + OUTPUT_UPLOAD_TABLE);

            for (String key : item_values.keySet())
            {
                String val = item_values.get(key).getS();
                logger.info(key + " : " + val);
            }

        } catch (ResourceNotFoundException e) {
            throw new IOException(e);
        } catch (AmazonServiceException e) {
            throw new IOException(e);
        }
    }

    @Override
    public CandidateRegistration getRegistration(String registrationId) throws IOException
    {
        DynamoDB dynamoDB = new DynamoDB(getAmazonDynamoDB());

        Table table = dynamoDB.getTable(REGISTRATION_TABLE_NAME);

        GetItemSpec spec = new GetItemSpec().withPrimaryKey(R_PROBLEM_GUID, registrationId);

        try {
            logger.info("Attempting to read the item: '" + registrationId + "' from table: '" + REGISTRATION_TABLE_NAME + "'");
            Item outcome = table.getItem(spec);
            logger.info("GetItem succeeded: " + outcome);

            Invitation i = new Invitation();
            i.setCandidateFirstName(outcome.getString(R_FIRST));
            i.setCandidateLastName(outcome.getString(R_LAST));
            i.setCandidateEmail(outcome.getString(R_EMAIL));
            i.setManagerEmail(outcome.getString(R_MGR_EMAIL));
            i.setInvitationDate(outcome.getString(R_DATE));

            CodingProblem cp = new CodingProblem();
            cp.setGuid(outcome.getString(R_PROBLEM_GUID));
            cp.setName(outcome.getString(R_PROBLEM_KEY));
            cp.setLandingPageUrl(outcome.getString(R_PROBLEM_LANDING_PAGE));
            cp.setSucceeded(outcome.getString(R_SUCCEEDED));
            cp.setAttempts(outcome.getInt(R_ATTEMPTS));

            CandidateRegistration cr = new CandidateRegistration();
            cr.setInvitation(i);
            cr.setProblemPage(cp);

            return cr;

        }
        catch (Exception e) {
            logger.error("Unable to read item: " + registrationId +
                    " from table: " + REGISTRATION_TABLE_NAME);
            logger.error(e.getMessage());

            throw new IOException(e);
        }
    }

    @Override
    public OutputResults getOutputResults(String uploadId) throws IOException
    {
        DynamoDB dynamoDB = new DynamoDB(getAmazonDynamoDB());

        Table table = dynamoDB.getTable(OUTPUT_UPLOAD_TABLE);

        GetItemSpec spec = new GetItemSpec().withPrimaryKey(O_UPLOAD_ID, uploadId);

        try {
            logger.info("Attempting to read the item: '" + uploadId + "' from table: '" + OUTPUT_UPLOAD_TABLE + "'");
            Item outcome = table.getItem(spec);
            logger.info("GetItem succeeded: " + outcome);

            if (null == outcome)
            {
                return null;
            }

            OutputResults or = new OutputResults();

            or.setInvitationId(outcome.getString(O_INVITATION_ID));
            or.setResults(outcome.getString(O_RESULT));
            or.setUploadDate(outcome.getString(O_OUTPUT_UPLOAD_DATE));
            or.setUploadID(outcome.getString(O_UPLOAD_ID));

            return or;

        }
        catch (Exception e) {
            logger.error("Unable to read item: " + uploadId +
                    " from table: " + OUTPUT_UPLOAD_TABLE);
            logger.error(e.getMessage());

            throw new IOException(e);
        }
    }
}
