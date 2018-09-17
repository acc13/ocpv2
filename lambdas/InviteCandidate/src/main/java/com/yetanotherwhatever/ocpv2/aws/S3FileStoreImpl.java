package com.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.yetanotherwhatever.ocpv2.IOutputStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by achang on 9/17/2018.
 */
public class S3FileStoreImpl implements IOutputStore{

    final AmazonS3 S3 = AmazonS3ClientBuilder.defaultClient();

    public void readFile(String bucketName, String key) throws IOException {
        S3Object s3object = S3.getObject(new GetObjectRequest(bucketName, key));

        System.out.println(s3object.getObjectMetadata().getContentType());
        System.out.println(s3object.getObjectMetadata().getContentLength());

        BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
        String line;
        while((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }


    @Override
    public InputStream getTestOutput() {
        return null;
    }

    @Override
    public InputStream getExpectedOutput() {
        return null;
    }
}
