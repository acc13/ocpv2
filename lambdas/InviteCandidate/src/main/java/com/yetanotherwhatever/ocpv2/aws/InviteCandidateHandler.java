package com.yetanotherwhatever.ocpv2.aws;


import com.yetanotherwhatever.ocpv2.Invitation;
import com.yetanotherwhatever.ocpv2.Inviter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;

import java.io.IOException;

/**
 * Created by achang on 9/3/2018.
 */

public class InviteCandidateHandler {

    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(InviteCandidateHandler.class);

    public String handleRequest(Invitation invitation, Context context) {

        try {
            new Inviter()
                    .setDB(new DynamoDB())
                    .setCodingProblem(new S3CodingProblem())
                    .setEmailer(new SESEmailHelper())
                    .sendInvitation(invitation);

            return "SUCCESS";
        }
        catch(IOException | IllegalArgumentException e)
        {
            logger.error(e);
            return "ERROR: " + e.getMessage();
        }


    }
}