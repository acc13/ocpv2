package com.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.yetanotherwhatever.ocpv2.IOcpV2DB;
import com.yetanotherwhatever.ocpv2.Invitation;
import com.yetanotherwhatever.ocpv2.Inviter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GetOutputTestResultsHandler {

    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(InviteCandidateHandler.class);

    public String handleRequest(String uploadId, Context context) {

        try {

            IOcpV2DB db = new DynamoOcpV2DB();
            OutputResults or = db.getOutputResults(uploadId);

            return or.getResults();
        }
        catch(IOException | IllegalArgumentException e)
        {
            logger.error(e);
            return "ERROR: " + e.getMessage();
        }


    }
}
