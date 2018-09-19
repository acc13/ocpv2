package com.yetanotherwhatever.ocpv2.aws;

import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.yetanotherwhatever.ocpv2.IOcpV2DB;
import com.yetanotherwhatever.ocpv2.Invitation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by achang on 9/3/2018.
 */
public class DynamoOcpV2DB implements IOcpV2DB {

    static final Logger logger = LogManager.getLogger(DynamoOcpV2DB.class);


    private static final String INVITE_TABLE_NAME = System.getenv("DYNAMODB_INVITE_TABLE");
    private static final String OUTPUT_UPLOAD_TABLE= System.getenv("DYNAMODB_OUTPUT_UPLOADS_TABLE");

    //Invitation table attributes
    private static final String I_FIRST = "First";
    private static final String I_LAST = "Last";
    private static final String I_EMAIL = "Email";
    private static final String I_MGR_EMAIL = "ManagerEmail";
    private static final String I_DATE = "CreatedDate";
    private static final String I_PROBLEM_KEY = "ProblemKey";
    private static final String I_PROBLEM_LANDING_PAGE= "LandingPageURL";
    private static final String I_PROBLEM_GUID= "ProblemPageGuid";    //table key
    private static final String I_SUCCEEDED = "Succeeded";
    private static final String I_ATTEMPTS = "Attempts";

    //Output upload table attributes
    private static final String O_UPLOAD_ID = "UploadId";
    private static final String O_INVITATION_ID = "ProblemPageGuid";
    private static final String O_RESULT = "Result";
    private static final String O_OUTPUT_UPLOAD_DATE = "UploadDate";

    private static final AmazonDynamoDB addb =
            AmazonDynamoDBClientBuilder.defaultClient();

    public DynamoOcpV2DB ()
    {

    }

    @Override
    public void write(Invitation i) throws IOException {

        HashMap<String,AttributeValue> item_values =
                new HashMap<>();

        item_values.put(I_FIRST, new AttributeValue(i.getCandidateFirstName()));
        item_values.put(I_LAST, new AttributeValue(i.getCandidateLastName()));
        item_values.put(I_EMAIL, new AttributeValue(i.getCandidateEmail()));
        item_values.put(I_MGR_EMAIL, new AttributeValue(i.getManagerEmail()));
        item_values.put(I_DATE, new AttributeValue(formatDateISO8601(i.getCreationDate())));

        item_values.put(I_PROBLEM_GUID, new AttributeValue(i.getProblemGuid()));
        item_values.put(I_PROBLEM_KEY, new AttributeValue(i.getProblemKey()));
        item_values.put(I_PROBLEM_LANDING_PAGE, new AttributeValue(i.getProblemLandingPageURL()));

        item_values.put(I_SUCCEEDED, new AttributeValue("Never"));

        item_values.put(I_ATTEMPTS, new AttributeValue().withN("0"));


        try {
            addb.putItem(INVITE_TABLE_NAME, item_values);

            logger.info("Invitation successfully saved to DynamoDB table " + INVITE_TABLE_NAME);

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

    //EST only
    static public String formatDateISO8601(Date d)
    {
        String nowAsISO = getISO8061DateFormat().format(d);

        return nowAsISO;
    }

    static public DateFormat getISO8061DateFormat()
    {
        String DEFAULT_TZ = "EST";
        String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm'" + DEFAULT_TZ + "'";
        TimeZone tz = TimeZone.getTimeZone(DEFAULT_TZ);
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setTimeZone(tz);

        return df;
    }

    public void updateInvitation(String invitationId, Date d, boolean success) throws IOException
    {

        try {
            DynamoDB ddb = new DynamoDB(addb);

            Table table = ddb.getTable(INVITE_TABLE_NAME);

            HashMap<String, String> expressionAttributeNames = new HashMap<String, String>();
            expressionAttributeNames.put("#A", I_ATTEMPTS);

            HashMap<String, Object> expressionAttributeValues = new HashMap<String, Object>();
            expressionAttributeValues.put(":val1", 1);

            //only update succeeded date if success
            String updateExpression;
            if (success)
            {
                expressionAttributeNames.put("#S", I_SUCCEEDED);
                expressionAttributeValues.put(":val2", formatDateISO8601(d));
                updateExpression = "set #A = #A + :val1, #S = :val2 "; // set last update time, increment attempts
            }
            //else just update attempts
            else
            {
                updateExpression = "set #A = #A + :val1"; // set last update time, increment attempts
            }

            logger.debug(updateExpression);

            table.updateItem(
                    I_PROBLEM_GUID, // key attribute name
                    invitationId,   // key attribute value
                    updateExpression,
                    expressionAttributeNames,
                    expressionAttributeValues);

            logger.debug("Invitation succeeded updated for record: " + invitationId);

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
        item_values.put(O_OUTPUT_UPLOAD_DATE, new AttributeValue(formatDateISO8601(or.getUploadDate())));


        try {
            addb.putItem(OUTPUT_UPLOAD_TABLE, item_values);

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
    public Invitation read(String invitationId)
    {
        DynamoDB dynamoDB = new DynamoDB(addb);

        Table table = dynamoDB.getTable(INVITE_TABLE_NAME);

        GetItemSpec spec = new GetItemSpec().withPrimaryKey(I_PROBLEM_GUID, invitationId);

        try {
            logger.info("Attempting to read the item...");
            Item outcome = table.getItem(spec);
            logger.info("GetItem succeeded: " + outcome);

            Invitation i = new Invitation();
            i.setCandidateFirstName(outcome.getString(I_FIRST));
            i.setCandidateLastName(outcome.getString(I_LAST));
            i.setCandidateEmail(outcome.getString(I_EMAIL));
            i.setManagerEmail(outcome.getString(I_MGR_EMAIL));
            i.set(outcome.getString(I_DATE));

            i.setProblemGuid(outcome.getString(I_PROBLEM_GUID));
            i.setProblemKey(outcome.getString(I_PROBLEM_KEY));
            i.setProblemLandingPageURL(outcome.getString(I_PROBLEM_LANDING_PAGE));

            i.set(outcome.getString(I_SUCCEEDED));

            i.set(outcome.getInt(I_ATTEMPTS));

        }
        catch (Exception e) {
            logger.error("Unable to read item: " + I_PROBLEM_GUID +
                    " from table: " + INVITE_TABLE_NAME);
            logger.error(e.getMessage());
        }
    }
}
