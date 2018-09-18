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

    static final AmazonS3 S3 = AmazonS3ClientBuilder.defaultClient();

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

        S3Object s3object = S3.getObject(new GetObjectRequest(bucketName, key));

        logger.debug("Object retrieved from S3: " + fileName);

        return s3object.getObjectContent();
    }
}
