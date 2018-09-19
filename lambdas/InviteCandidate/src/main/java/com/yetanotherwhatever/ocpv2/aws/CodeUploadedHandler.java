package com.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.yetanotherwhatever.ocpv2.CodeUploadedNotifier;
import com.yetanotherwhatever.ocpv2.OutputChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.function.Function;

/**
 * Created by achang on 9/12/2018.
 */
public class CodeUploadedHandler  implements RequestHandler<S3Event,S3Event> {

    static final Logger logger = LogManager.getLogger(CodeUploadedHandler.class);

    String zipFileName, invitationId;

    public S3Event handleRequest(S3Event s3Event, Context context) {

        // For each record.
        for (S3EventNotification.S3EventNotificationRecord record : s3Event.getRecords()) {
            try {

                CodeUploadedNotifier un = new CodeUploadedNotifier();
                un.setDb(new DynamoOcpV2DB());
                un.setEmailer(new SESEmailHelper());
                un.setFileStore(new S3FileStoreImpl());

                un.notifyManager(invitationId, zipFileName);

                logger.info("Manager notified.");

            } catch (IOException | IllegalArgumentException e) {
                logger.error(e);
            }
        }

        //notify code upload
        logger.debug("Code upload notification complete.");

        return s3Event;
    }

    public void parseRecord(S3EventNotification.S3EventNotificationRecord record)
    {
        ///////////////////////////////
        //  EXTRACT TEST FILENAME
        ///////////////////////////////
        String bucketName = record.getS3().getBucket().getName();
        String testKey = record.getS3().getObject().getKey();

        this.zipFileName = bucketName + ":" + testKey;

        logger.debug("Test output file created in S3: " + zipFileName );


        ///////////////////////////////
        //  EXTRACT INVITATION ID
        ///////////////////////////////
        String[] parts =testKey.split("/");
        //test key should look like: "uploads/code/<invitation Id>/<random uuid>.zip"
        if (parts.length != 4)
        {
            throw new IllegalArgumentException("Malformed output upload key: " + testKey);
        }

        this.invitationId = parts[2];

    }
}
