package com.yetanotherwhatever.ocpv2.lambdas;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by achang on 9/3/2018.
 */
class S3CodingProblem implements ICodingProblem {

    final AmazonS3 S3 = AmazonS3ClientBuilder.defaultClient();

    private static final String S3_WEB_BUCKET = System.getenv("S3_WEB_BUCKET");
    private static final String PROBLEMS_PREFIX = "problems/";
    private static final String TEMP_PAGE_PREFIX = "tp/";

    private String problemKey;
    private String problemGuid;
    private String landingPageURL;

    public String getLandingPageURL() {

        return landingPageURL;
    }

    public String getProblemKey() {
        return problemKey;
    }

    public String getProblemGuid() {
        return problemGuid;
    }

    protected S3CodingProblem()
    {
        problemGuid = UUID.randomUUID().toString();
    }

    public void setup() throws IOException
    {
        try {
            problemKey = pickAProblem();

            //copy files
            String destKey = copyProblem();

            //build url
            landingPageURL = "https://"+ S3_WEB_BUCKET + "/" + destKey;

        } catch (AmazonServiceException e)
        {
            throw new IOException(e);
        }
    }

    private String pickAProblem()
    {
        ObjectListing object_listing = S3.listObjects(S3_WEB_BUCKET, PROBLEMS_PREFIX);

        int objCount = object_listing.getObjectSummaries().size();
        int randIndex = pickOne(objCount);
        S3ObjectSummary summary = object_listing.getObjectSummaries().get(randIndex);
        return summary.getKey();
    }

    protected static int pickOne(int size)
    {
        int r = ThreadLocalRandom.current().nextInt(size);

        return r;
    }

    private String copyProblem()
    {
        String destKey = TEMP_PAGE_PREFIX + problemGuid + ".html";

        S3.copyObject(S3_WEB_BUCKET, problemKey, S3_WEB_BUCKET, destKey);

        return destKey;
    }

}