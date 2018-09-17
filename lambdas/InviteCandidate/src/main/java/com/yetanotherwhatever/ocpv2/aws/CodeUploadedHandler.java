package com.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.yetanotherwhatever.ocpv2.Invitation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by achang on 9/12/2018.
 */
public class CodeUploadedHandler {


    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(InviteCandidateHandler.class);

    public String handleRequest(Invitation invitation, Context context) {

        //notify code upload
        logger.debug("Code upload notification complete.");

        return "SUCCESS";


    }
}
