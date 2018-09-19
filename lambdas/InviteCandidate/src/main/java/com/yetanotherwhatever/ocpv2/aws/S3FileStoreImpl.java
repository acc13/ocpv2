package com.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.yetanotherwhatever.ocpv2.IFileStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by achang on 9/17/2018.
 */
public class S3FileStoreImpl implements IFileStore {

    static final Logger logger = LogManager.getLogger(S3FileStoreImpl.class);

    static AmazonS3 S3;

    S3FileStoreImpl()
    {
    }

    public InputStream readFile(String fileName) throws IOException {

        if (null == S3) {
            S3 = AmazonS3ClientBuilder.defaultClient();
        }

        String[] parts = fileName.split(":");

        if (parts.length != 2)
        {
            throw new IllegalArgumentException("Invalid filename encountered: " + fileName);
        }

        String bucketName = parts[0];
        String key= parts[1];

        S3Object s3object = S3.getObject(new GetObjectRequest(bucketName, key));

        logger.debug("Object retrieved from S3: " + fileName);

        return s3object.getObjectContent();
    }

    @Override
    public String buildDownloadUrl(String fileName) throws IOException
    {
        if (null == fileName)
        {
            throw new IllegalArgumentException("fileName cannot be null.");
        }

        String[] parts = fileName.split(":");
        if (parts.length != 2)
        {
            throw new IllegalArgumentException("Invalid fileName encountered: " + fileName);
        }

        String bucket = parts[0];
        String key = parts[1];

        //Example:
        //https://s3.amazonaws.com/test.upload.yetanotherwhatever.io/uploads/code/63ba691e-3a9d-4c26-a22f-735cd4b83328/135A04E5-F2A4-49C4-B298-D9E29BB7BBCC.zip

        String url =  "https://s3.amazonaws.com/" + bucket + "/" + key;
        return url;
    }
}
