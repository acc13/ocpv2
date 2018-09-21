package com.yetanotherwhatever.ocpv2.aws;


import com.yetanotherwhatever.ocpv2.Invitation;
import com.yetanotherwhatever.ocpv2.Inviter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by achang on 9/3/2018.
 */

public class InviteCandidateHandler {

    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(InviteCandidateHandler.class);

    public String handleRequest(Invitation invitation, Context context) {

        JSONObject responseJson = new JSONObject();

        try {
            new Inviter()
                    .setDB(new DynamoOcpV2DB())
                    .setCodingProblem(new S3CodingProblem())
                    .setEmailer(new SESEmailHelper())
                    .sendInvitation(invitation);


            JSONObject headerJson = new JSONObject();
            //headerJson.put("x-custom-header", "my custom header value");

            responseJson.put("isBase64Encoded", false);
            responseJson.put("statusCode", 200);
            responseJson.put("headers", headerJson);
            responseJson.put("body", "{Success}");

        }
        catch(IOException | IllegalArgumentException e)
        {
            logger.error(e);
            responseJson.put("exception", e);
        }


        logger.debug(responseJson.toJSONString());
        return responseJson.toJSONString();

    }
}