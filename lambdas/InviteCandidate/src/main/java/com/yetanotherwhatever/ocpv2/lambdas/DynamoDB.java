package com.yetanotherwhatever.ocpv2.lambdas;

import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Created by achang on 9/3/2018.
 */
public class DynamoDB implements IOcpV2DB {

    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(IOcpV2DB.class);


    private static final String INVITE_TABLE_NAME = System.getenv("DYNAMODB_INVITE_TABLE");

    private static final String FIRST = "First";
    private static final String LAST = "Last";
    private static final String EMAIL = "Email";
    private static final String MGR_EMAIL = "ManagerEmail";
    private static final String DATE = "Date";

    private static final String PROBLEM_KEY = "ProblemKey";
    private static final String PROBLEM_LANDING_PAGE= "LandingPageURL";
    private static final String PROBLEM_GUID= "ProblemPageGuid";

    private static final AmazonDynamoDB ddb =
            AmazonDynamoDBClientBuilder.defaultClient();  //aws lambda sdk > 1.9.6
            //new AmazonDynamoDBClient(); //aws lambda sdk 1.9.6

    public DynamoDB ()
    {

    }

    @Override
    public void write(Invitation i) throws IOException {

        HashMap<String,AttributeValue> item_values =
                new HashMap<>();

        item_values.put(FIRST, new AttributeValue(i.getCandidateFirstName()));
        item_values.put(LAST, new AttributeValue(i.getCandidateLastName()));
        item_values.put(EMAIL, new AttributeValue(i.getCandidateEmail()));
        item_values.put(MGR_EMAIL, new AttributeValue(i.getManagerEmail()));
        item_values.put(DATE, new AttributeValue(i.getCreationDate().toString()));

        item_values.put(PROBLEM_GUID, new AttributeValue(i.getProblemGuid()));
        item_values.put(PROBLEM_KEY, new AttributeValue(i.getProblemKey()));
        item_values.put(PROBLEM_LANDING_PAGE, new AttributeValue(i.getProblemLandingPageURL()));

        try {
            ddb.putItem(INVITE_TABLE_NAME, item_values);
        } catch (ResourceNotFoundException e) {
            throw new IOException(e);
        } catch (AmazonServiceException e) {
            throw new IOException(e);
        }
    }
}
