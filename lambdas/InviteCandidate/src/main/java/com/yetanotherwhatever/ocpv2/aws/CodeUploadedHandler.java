package com.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.model.S3Event;
import com.yetanotherwhatever.ocpv2.Invitation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

/**
 * Created by achang on 9/12/2018.
 */
public class CodeUploadedHandler  implements RequestHandler<S3Event,S3Event> {


    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(InviteCandidateHandler.class);

    public S3Event handleRequest(S3Event s3Event, Context context) {

        //notify code upload
        logger.debug("Code upload notification complete.");

        return s3Event;
    }
}
