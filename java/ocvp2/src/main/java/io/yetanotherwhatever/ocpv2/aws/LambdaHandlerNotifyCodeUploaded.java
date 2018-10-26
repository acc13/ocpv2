package io.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import io.yetanotherwhatever.ocpv2.CodeUploadedNotifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;

/**
 * Created by achang on 9/12/2018.
 */
public class LambdaHandlerNotifyCodeUploaded implements RequestHandler<S3Event,S3Event> {

    static final Logger logger = LogManager.getLogger(LambdaHandlerNotifyCodeUploaded.class);

    protected CodeUploadedNotifier codeUploadNotifier = null;

    protected S3FileStoreImpl s3FileStore = null;

    public S3Event handleRequest(S3Event s3Event, Context context) {

        // For each record.
        for (S3EventNotification.S3EventNotificationRecord record : s3Event.getRecords()) {
            try {

                CodeUploadedNotifier un = getCodeUploadedNotifier();
                un.setDb(new DynamoOcpV2DB());
                un.setEmailer(new SESEmailHelper());
                un.setFileStore(new S3FileStoreImpl());

                String invitationId = extractInvitationId(record);
                String downloadUrl = getS3FileStore().buildDownloadUrl(record);

                un.notifyManager(invitationId, downloadUrl);

                logger.info("Manager notified.");
                logger.debug("Code upload notification complete.");

            } catch (IOException | IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return s3Event;
    }

    //for unit testing
    protected S3FileStoreImpl getS3FileStore()
    {
        if (null == s3FileStore)
        {
            s3FileStore = new S3FileStoreImpl();
        }

        return s3FileStore;
    }

    protected CodeUploadedNotifier getCodeUploadedNotifier()
    {
        if (null == this.codeUploadNotifier)
        {
            this.codeUploadNotifier = new CodeUploadedNotifier();
        }

        return this.codeUploadNotifier;
    }

    private String extractInvitationId(S3EventNotification.S3EventNotificationRecord record)
    {
        String testKey = record.getS3().getObject().getKey();
        String[] pathParts =testKey.split("/");
        //test key should look like: "uploads/code/<invitation Id>/<random uuid>.zip"
        if (pathParts.length != 4)
        {
            throw new IllegalArgumentException("Malformed output upload key: " + testKey);
        }

        String invitationId = pathParts[2];

        return invitationId;

    }
}
