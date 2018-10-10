package io.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
import io.yetanotherwhatever.ocpv2.CodeUploadedNotifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by achang on 9/12/2018.
 */
public class LambdaHandlerNotifyCodeUploaded implements RequestHandler<S3Event,S3Event> {

    static final Logger logger = LogManager.getLogger(LambdaHandlerNotifyCodeUploaded.class);

    CodeUploadedNotifier cun = null;

    public S3Event handleRequest(S3Event s3Event, Context context) {

        // For each record.
        for (S3EventNotification.S3EventNotificationRecord record : s3Event.getRecords()) {
            try {

                CodeUploadedNotifier un = getCodeUploadedNotifier();
                un.setDb(new DynamoOcpV2DB());
                un.setEmailer(new SESEmailHelper());
                un.setFileStore(new S3FileStoreImpl());

                String invitationId = extractInvitationId(record);
                String downloadUrl = extractS3DownloadUrl(record);

                un.notifyManager(invitationId, downloadUrl);

                logger.info("Manager notified.");
                logger.debug("Code upload notification complete.");

            } catch (IOException | IllegalArgumentException e) {
                logger.error(e);
            }
        }

        return s3Event;
    }

    protected void setCodeUploadedNotifier(CodeUploadedNotifier cun)
    {
        this.cun = cun;
    }

    protected CodeUploadedNotifier getCodeUploadedNotifier()
    {
        if (null == this.cun)
        {
            this.cun = new CodeUploadedNotifier();
        }

        return this.cun;
    }

    private String extractS3DownloadUrl(S3EventNotification.S3EventNotificationRecord record)
    {
        String bucket = record.getS3().getBucket().getName();
        String key = record.getS3().getObject().getKey();

        logger.debug("Code upload at s3 bucket: '" + bucket + "' key: '" + key + "'");

        String downloadUrl = S3FileStoreImpl.buildDownloadUrl(bucket, key);

        logger.debug("Download URL: " + downloadUrl);

        return downloadUrl;
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
