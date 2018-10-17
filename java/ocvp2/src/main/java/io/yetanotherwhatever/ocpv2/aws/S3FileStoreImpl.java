package io.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import io.yetanotherwhatever.ocpv2.IFileStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by achang on 9/17/2018.
 */
public class S3FileStoreImpl implements IFileStore {

    static final Logger logger = LogManager.getLogger(S3FileStoreImpl.class);

    private static  AmazonS3 S3;

    private static final String CLIENT_REGION = "us-east-1";

    S3FileStoreImpl()
    {
    }

    public InputStream readFile(String fileName) throws IOException {


        String[] parts = fileName.split(":");

        if (parts.length != 2)
        {
            throw new IllegalArgumentException("Invalid filename encountered: " + fileName);
        }

        String bucketName = parts[0];
        String key= parts[1];

        S3Object s3object = getS3().getObject(new GetObjectRequest(bucketName, key));

        logger.debug("Object retrieved from S3: " + fileName);

        return s3object.getObjectContent();
    }

    private static AmazonS3 getS3()
    {
        if (null == S3) {
            S3 = AmazonS3ClientBuilder.standard()
                    .build();
        }

        return S3;
    }

    public String buildDownloadUrl(S3EventNotification.S3EventNotificationRecord record)
    {
        String bucket = record.getS3().getBucket().getName();
        String key = record.getS3().getObject().getKey();
        return buildDownloadUrl(bucket, key);
    }


    public String buildDownloadUrl(String bucket, String key)
    {
        //Example:
        //https://s3.amazonaws.com/test.upload.yetanotherwhatever.io/uploads/code/63ba691e-3a9d-4c26-a22f-735cd4b83328/135A04E5-F2A4-49C4-B298-D9E29BB7BBCC.zip
        String url =  "https://s3.amazonaws.com/" + bucket + "/" + key;
        return url;
    }
}
