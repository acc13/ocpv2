package io.yetanotherwhatever.ocpv2.aws;


import io.yetanotherwhatever.ocpv2.*;
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

    private IOcpV2DB db;
    private ICodingProblemBuilder problemBuilder;
    private IEmailer emailHelper;

    private JSONObject lastResponseJson;

    public LambdaHandlerInviteCandidate()
    {
        db = new DynamoOcpV2DB();
        problemBuilder = new S3CodingProblemBuilder();
        emailHelper = new SESEmailHelper();
    }

    //for test injection
    protected LambdaHandlerInviteCandidate(IOcpV2DB db,
                                 ICodingProblemBuilder problemBuilder,
                                 IEmailer emailHelper)
    {
        this.db = db;
        this.problemBuilder = problemBuilder;
        this.emailHelper = emailHelper;
    }

    public String handleRequest(Invitation invitation, Context context) {


        try {
            new Inviter()
                    .setDB(db)
                    .setCodingProblemBuilder(problemBuilder)
                    .setEmailer(emailHelper)
                    .sendInvitation(invitation);

        }
        catch(IOException e)
        {
            logger.error(e.getMessage(), e);
            return buildResponse(500, e);
        }
        catch (IllegalArgumentException e)
        {
            logger.error(e.getMessage(), e);
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

        //for testing
        lastResponseJson = responseJson;

        return responseJson.toJSONString();
    }

    protected JSONObject getLastResponseJson()
    {
        return lastResponseJson;
    }
}