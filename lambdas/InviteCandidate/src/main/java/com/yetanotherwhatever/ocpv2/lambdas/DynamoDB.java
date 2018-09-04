package com.yetanotherwhatever.ocpv2.lambdas;

import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import java.util.HashMap;

/**
 * Created by achang on 9/3/2018.
 */
public class DynamoDB implements IOcpV2DB {

    //table name
    private static final String INVITE_TABLE_NAME = "Invitations";
    //table columns
    private static final String FIRST = "First";
    private static final String LAST = "Last";
    private static final String EMAIL = "Email";
    private static final String MGR_EMAIL = "MgrEmail";
    private static final String DATE = "Date";

    private final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

    public DynamoDB ()
    {

    }

    @Override
    public void write(Invitation i) throws IOException {

        HashMap<String,AttributeValue> item_values =
                new HashMap<>();

        item_values.put(FIRST, new AttributeValue(i.getFirst()));
        item_values.put(LAST, new AttributeValue(i.getLast()));
        item_values.put(EMAIL, new AttributeValue(i.getEmail()));
        item_values.put(MGR_EMAIL, new AttributeValue(i.getManagerEmail()));
        item_values.put(DATE, new AttributeValue(i.getDate().toString()));


        try {
            ddb.putItem(INVITE_TABLE_NAME, item_values);
        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The table \"%s\" can't be found.\n", INVITE_TABLE_NAME);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            throw new IOException("Exception occurred while saving invitation to Dynamo DB", e);
        } catch (AmazonServiceException e) {
            System.err.println(e.getMessage());
            throw new IOException("Exception occurred while saving invitation to Dynamo DB", e);
        }
    }
}
