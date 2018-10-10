package io.yetanotherwhatever.ocpv2.aws;


import io.yetanotherwhatever.ocpv2.Invitation;
import io.yetanotherwhatever.ocpv2.Inviter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * Created by achang on 9/3/2018.
 */

public class LambdaHandlerInviteCandidate {

    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(LambdaHandlerInviteCandidate.class);

    public String handleRequest(Invitation invitation, Context context) {


        try {
            new Inviter()
                    .setDB(new DynamoOcpV2DB())
                    .setCodingProblemBuilder(new S3CodingProblemBuilderBuilder())
                    .setEmailer(new SESEmailHelper())
                    .sendInvitation(invitation);



        }
        catch(IOException e)
        {
            logger.error(e);
            return buildResponse(500, e);
        }
        catch (IllegalArgumentException e)
        {
            logger.error(e);
            return buildResponse(400, e);
        }


        return buildResponse(200);
    }

    private String buildResponse(int responseCode)
    {
        Exception e = null;
        return buildResponse(responseCode, e);
    }

    private String buildResponse(int responseCode, Exception e)
    {
        JSONObject responseJson = new JSONObject();

        if (null != e)
        {
            responseJson.put("exception", e);
        }

        responseJson.put("statusCode", responseCode);
        responseJson.put("isBase64Encoded", false);
        JSONObject headerJson = new JSONObject();
        //headerJson.put("x-custom-header", "my custom header value");
        responseJson.put("headers", headerJson);
        responseJson.put("body", "{Success}");

        logger.debug(responseJson.toJSONString());

        return responseJson.toJSONString();
    }
}