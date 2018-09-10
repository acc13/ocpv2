package com.yetanotherwhatever.ocpv2.lambdas;


import com.amazonaws.services.kinesis.model.InvalidArgumentException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by achang on 9/3/2018.
 */

public class InviteCandidateHandler implements RequestHandler<Invitation, String>{

    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(InviteCandidateHandler.class);

    @Override
    public String handleRequest(Invitation invitation, Context context) {

        try {
            new Inviter()
                    .setDB(new DynamoDB())
                    .setCodingProblem(new S3CodingProblem())
                    .setEmailer(new SESEmailHelper())
                    .sendInvitation(invitation);

            return "SUCCESS";
        }
        catch(IOException | InvalidArgumentException e)
        {
            logger.error(e);
            return "ERROR: " + e.getMessage();
        }


    }
}