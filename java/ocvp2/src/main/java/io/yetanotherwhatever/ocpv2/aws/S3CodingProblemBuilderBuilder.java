package io.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import io.yetanotherwhatever.ocpv2.CodingProblem;
import io.yetanotherwhatever.ocpv2.ICodingProblemBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by achang on 9/3/2018.
 */
public class S3CodingProblemBuilderBuilder implements ICodingProblemBuilder {

    static final Logger logger = LogManager.getLogger(S3CodingProblemBuilderBuilder.class);

    final AmazonS3 S3 = AmazonS3ClientBuilder.defaultClient();

    private static final String S3_WEB_BUCKET = System.getenv("S3_WEB_BUCKET");
    private static final String PROBLEMS_PREFIX = "problems/";
    private static final String TEMP_PAGE_PREFIX = "tp/";

    protected S3CodingProblemBuilderBuilder()
    {

    }

    @Override
    public CodingProblem buildCodingProblem() throws IOException
    {
        try {
            CodingProblem cp = new CodingProblem();

            String problemKey = pickAProblem();
            cp.setName(problemKey);

            //copy files
            String problemGuid = UUID.randomUUID().toString();
            String destKey = copyProblem(problemGuid, problemKey);
            cp.setGuid(problemGuid);

            //build url
            String landingPageURL = "http://"+ S3_WEB_BUCKET + "/" + destKey;
            cp.setLandingPageUrl(landingPageURL);

            cp.setSucceeded("Never");
            cp.setAttempts(0);

            logger.info("Landing page URL: " + landingPageURL);

            return cp;

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

        logger.info("Problem " + summary.getKey() + " selected.");

        return summary.getKey();
    }

    protected static int pickOne(int size)
    {
        int r = ThreadLocalRandom.current().nextInt(size);

        return r;
    }

    private String copyProblem(String problemGuid, String problemKey)
    {
        String destKey = TEMP_PAGE_PREFIX + problemGuid + ".html";

        CopyObjectRequest cor = new CopyObjectRequest(S3_WEB_BUCKET, problemKey, S3_WEB_BUCKET, destKey);
        cor.setCannedAccessControlList(CannedAccessControlList.PublicRead);

        S3.copyObject(cor);

        logger.info("Problem copied to key: " + destKey);

        return destKey;
    }

}
