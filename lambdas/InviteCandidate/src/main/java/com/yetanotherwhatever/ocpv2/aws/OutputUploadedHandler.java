package com.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.yetanotherwhatever.ocpv2.Invitation;
import com.yetanotherwhatever.ocpv2.OutputChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by achang on 9/12/2018.
 */
public class OutputUploadedHandler {


    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(InviteCandidateHandler.class);

    public String handleRequest(Invitation invitation, Context context) {

        try {
            OutputChecker.checkOutput();

            logger.debug("Output check complete.");

            return "SUCCESS";
        }
        catch(IOException | IllegalArgumentException e)
        {
            logger.error(e);
            return "ERROR: " + e.getMessage();
        }


    }
}
