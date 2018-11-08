package io.yetanotherwhatever.ocpv2.aws;

import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import io.yetanotherwhatever.ocpv2.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by achang on 9/3/2018.
 */
public class DynamoOcpV2DB implements IOcpV2DB {

    static final Logger logger = LogManager.getLogger(DynamoOcpV2DB.class);


    private static final String REGISTRATION_TABLE_NAME = System.getenv("DYNAMODB_REGISTRATION_TABLE");
    private static final String OUTPUT_UPLOAD_TABLE= System.getenv("DYNAMODB_OUTPUT_UPLOADS_TABLE");

    //Registtration table attributes
    private static final String I_FIRST = "First";
    private static final String I_LAST = "Last";
    private static final String I_EMAIL = "Email";
    private static final String I_MGR_EMAIL = "ManagerEmail";
    private static final String I_DATE = "CreatedDate";
    private static final String I_TYPE = "Type";
    private static final String I_RESUME = "Resume";
    private static final String CP_PROBLEM_KEY = "ProblemKey";
    private static final String CP_PROBLEM_LANDING_PAGE = "LandingPageURL";
    private static final String CP_PROBLEM_GUID_PRIMARY_KEY = "ProblemPageGuid";    //table key
    private static final String CP_EXPIRATION_DATE = "PageExpirationDate";
    private static final String H_SUCCEEDED = "Succeeded";
    private static final String H_ATTEMPTS = "Attempts";
    private static final String H_CODE_URL = "CodingSolutionUrl";

    //Output upload table attributes
    private static final String O_UPLOAD_ID = "UploadId";
    private static final String O_INVITATION_ID = CP_PROBLEM_GUID_PRIMARY_KEY;
    private static final String O_RESULT = "Result";
    private static final String O_OUTPUT_UPLOAD_DATE = "UploadDate";

    private static final String EMPTY_STRING = "EMPTY_STRING";
    private static final String NULL_VAL = "NULL_VAL";

    private static AmazonDynamoDB addb;

    public DynamoOcpV2DB ()
    {

    }

    private AmazonDynamoDB getAmazonDynamoDB()
    {
        //lazy load
        if (null == addb)
        {
            addb = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .build();
        }

        return addb;
    }

    @Override
    public void write(CandidateWorkflow cr) throws IOException {

        HashMap<String,AttributeValue> item_values =
                new HashMap<>();

        Invitation i = cr.getInvitation();
        addAttributeSValue(item_values, I_FIRST, i.getCandidateFirstName());
        addAttributeSValue(item_values, I_LAST, i.getCandidateLastName());
        addAttributeSValue(item_values, I_EMAIL, i.getCandidateEmail());
        addAttributeSValue(item_values, I_MGR_EMAIL, i.getManagerEmail());
        addAttributeSValue(item_values, I_DATE, i.getInvitationDate());
        addAttributeNValue(item_values, I_TYPE, i.getType().getValue());
        addAttributeSValue(item_values, I_RESUME, i.getResumeUrl());

        CodingProblem cp = cr.getCodingProblem();
        addAttributeSValue(item_values, CP_PROBLEM_GUID_PRIMARY_KEY, cp.getGuid());
        addAttributeSValue(item_values, CP_PROBLEM_KEY, cp.getName());
        addAttributeSValue(item_values, CP_PROBLEM_LANDING_PAGE, cp.getLandingPageUrl());
        addAttributeSValue(item_values, CP_EXPIRATION_DATE, cp.getExpirationDate());

        OutputTestHistory oth = cr.getOutputTestHistory();
        addAttributeSValue(item_values, H_SUCCEEDED, oth.getSucceeded());
        addAttributeNValue(item_values, H_ATTEMPTS, oth.getAttempts());
        addAttributeSValue(item_values, H_CODE_URL, oth.getCodeSolutionUrl());


        try {
            getAmazonDynamoDB().putItem(REGISTRATION_TABLE_NAME, item_values);

            logger.info("Registration successfully saved to DynamoDB table " + REGISTRATION_TABLE_NAME);

            for (String key : item_values.keySet())
            {
                String val = item_values.get(key).toString();
                logger.info(key + " : " + val);
            }

        } catch (ResourceNotFoundException e) {
            throw new IOException(e);
        } catch (AmazonServiceException e) {
            throw new IOException(e);
        }
    }

    //deletes by primary key
    //other fields will be ignored
    public void delete(CandidateWorkflow cr) {


        DynamoDB ddb = new DynamoDB(getAmazonDynamoDB());

        Table table = ddb.getTable(REGISTRATION_TABLE_NAME);

        table.deleteItem(CP_PROBLEM_GUID_PRIMARY_KEY, cr.getCodingProblem().getGuid());
    }

    //deletes by primary key
    //other fields will be ignored
    public void delete(OutputResults or) {


        DynamoDB ddb = new DynamoDB(getAmazonDynamoDB());

        Table table = ddb.getTable(OUTPUT_UPLOAD_TABLE);

        table.deleteItem(O_UPLOAD_ID, or.getUploadID());
    }

    private void addAttributeNValue(HashMap<String, AttributeValue> values, String key, int val)
    {
        values.put(key, new AttributeValue().withN(Integer.toString(val)));
    }


    private void addAttributeSValue(HashMap<String, AttributeValue> values, String key, String val)
    {

        if (null == val)
        {
            val = NULL_VAL;
        }

        if (val.length() == 0)
        {
            val = EMPTY_STRING;
        }

        values.put(key, new AttributeValue(val));
    }

    @Override
    public void updateOutputTestHistory(String problemPageId, String outputUploadDate, boolean success) throws IOException
    {

        try {
            DynamoDB ddb = new DynamoDB(getAmazonDynamoDB());

            Table table = ddb.getTable(REGISTRATION_TABLE_NAME);

            HashMap<String, String> expressionAttributeNames = new HashMap<String, String>();
            expressionAttributeNames.put("#A", H_ATTEMPTS);

            HashMap<String, Object> expressionAttributeValues = new HashMap<String, Object>();
            expressionAttributeValues.put(":val1", 1);

            //only update succeeded date if success
            String updateExpression;
            if (success)
            {
                expressionAttributeNames.put("#S", H_SUCCEEDED);
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
                    CP_PROBLEM_GUID_PRIMARY_KEY, // key attribute name
                    problemPageId,   // key attribute value
                    updateExpression,
                    expressionAttributeNames,
                    expressionAttributeValues);

            logger.debug("Invitation update succeeded for record: " + problemPageId);

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

        addAttributeSValue(item_values, O_UPLOAD_ID, or.getUploadID());
        addAttributeSValue(item_values, O_INVITATION_ID, or.getInvitationId());
        addAttributeSValue(item_values, O_RESULT, or.getResults());
        addAttributeSValue(item_values, O_OUTPUT_UPLOAD_DATE, or.getUploadDate());


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
    public CandidateWorkflow getWorkflow(String problemPageId) throws IOException
    {
        DynamoDB dynamoDB = new DynamoDB(getAmazonDynamoDB());

        Table table = dynamoDB.getTable(REGISTRATION_TABLE_NAME);

        GetItemSpec spec = new GetItemSpec().withPrimaryKey(CP_PROBLEM_GUID_PRIMARY_KEY, problemPageId);

        try {
            logger.info("Attempting to read the item: '" + problemPageId + "' from table: '" + REGISTRATION_TABLE_NAME + "'");
            Item outcome = table.getItem(spec);
            logger.info("GetItem succeeded: " + outcome);

            CandidateWorkflow workflow = itemToCandidateWorkflow(outcome);

            return workflow;

        }
        catch (Exception e) {
            logger.error("Unable to read item: " + problemPageId +
                    " from table: " + REGISTRATION_TABLE_NAME);
            logger.error(e.getMessage());

            throw new IOException(e);
        }
    }

    private String getItemString(Item i, String key)
    {
        String val = i.getString(key);

        if (NULL_VAL.equals(val))
        {
            return null;
        }

        if (EMPTY_STRING.equals(val))
        {
            return "";
        }

        return val;
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

            or.setInvitationId(getItemString(outcome, O_INVITATION_ID));
            or.setResults(getItemString(outcome, O_RESULT));
            or.setUploadDate(getItemString(outcome, O_OUTPUT_UPLOAD_DATE));
            or.setUploadID(getItemString(outcome, O_UPLOAD_ID));

            return or;

        }
        catch (Exception e) {
            logger.error("Unable to read item: " + uploadId +
                    " from table: " + OUTPUT_UPLOAD_TABLE);
            logger.error(e.getMessage());

            throw new IOException(e);
        }
    }

    public List<CandidateWorkflow> listAllInterns()
    {
        DynamoDB dynamoDB = new DynamoDB(getAmazonDynamoDB());

        Table table = dynamoDB.getTable(REGISTRATION_TABLE_NAME);

        Map<String, Object> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":type", Invitation.Type.INTERN.getValue());
        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#type", I_TYPE);

        ItemCollection<ScanOutcome> items = table.scan("#type = :type", // FilterExpression
                expressionAttributeNames,
                expressionAttributeValues);

        Iterator<Item> iterator = items.iterator();
        ArrayList<CandidateWorkflow> wfList = new ArrayList<>();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            wfList.add(itemToCandidateWorkflow(item));
        }

        return wfList;
    }

    private CandidateWorkflow itemToCandidateWorkflow(Item item)
    {
        Invitation invite = new Invitation();
        invite.setCandidateFirstName(getItemString(item, I_FIRST));
        invite.setCandidateLastName(getItemString(item, I_LAST));
        invite.setCandidateEmail(getItemString(item, I_EMAIL));
        invite.setManagerEmail(getItemString(item, I_MGR_EMAIL));
        invite.setInvitationDate(getItemString(item, I_DATE));
        invite.setType(Invitation.Type.fromInt(item.getInt(I_TYPE)));
        invite.setResumeUrl(getItemString(item, I_RESUME));

        CodingProblem problem = new CodingProblem();
        problem.setGuid(getItemString(item, CP_PROBLEM_GUID_PRIMARY_KEY));
        problem.setName(getItemString(item, CP_PROBLEM_KEY));
        problem.setLandingPageUrl(getItemString(item, CP_PROBLEM_LANDING_PAGE));
        problem.setExpirationDate(getItemString(item, CP_EXPIRATION_DATE));

        OutputTestHistory history = new OutputTestHistory();
        history.setSucceeded(getItemString(item, H_SUCCEEDED));
        history.setAttempts(item.getInt(H_ATTEMPTS));
        history.setCodeSolutionUrl(getItemString(item, H_CODE_URL));

        return new CandidateWorkflow(invite, problem, history);
    }
}
