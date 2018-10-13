package io.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.lifecycle.LifecyclePrefixPredicate;
import io.yetanotherwhatever.ocpv2.CodingProblem;
import io.yetanotherwhatever.ocpv2.ICodingProblemBuilder;
import io.yetanotherwhatever.ocpv2.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by achang on 9/3/2018.
 */
public class S3CodingProblemBuilder implements ICodingProblemBuilder {

    static final Logger logger = LogManager.getLogger(S3CodingProblemBuilder.class);

    final AmazonS3 S3 = AmazonS3ClientBuilder.defaultClient();

    private static final String S3_WEB_BUCKET = System.getenv("S3_WEB_BUCKET");
    private static final String PROBLEMS_PREFIX = "problems/";
    private static final String TEMP_PAGE_PREFIX = "tp/";
    int expirationInDays;

    protected S3CodingProblemBuilder() throws IllegalStateException
    {
        getS3WebBucket();

        setExpirationInDays();
    }

    static protected String getS3WebBucket()
    {
        if (null == S3_WEB_BUCKET || S3_WEB_BUCKET.length() == 0)
        {
            throw new IllegalStateException("S3_WEB_BUCKET env var not set");
        }

        return S3_WEB_BUCKET;
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
            logger.info("Landing page URL: " + landingPageURL);

            return cp;

        } catch (AmazonServiceException e)
        {
            throw new IOException(e);
        }
    }

    private void setExpirationInDays()
    {
        BucketLifecycleConfiguration lifeCycleConfig = S3.getBucketLifecycleConfiguration(S3_WEB_BUCKET);

        List<BucketLifecycleConfiguration.Rule> rules = lifeCycleConfig.getRules();

        int days = rules.stream()
                .filter(e -> e.getFilter().getPredicate() instanceof LifecyclePrefixPredicate)
                .filter(e -> ((LifecyclePrefixPredicate)e.getFilter().getPredicate()).getPrefix().equals(TEMP_PAGE_PREFIX))
                .map(e -> e.getExpirationInDays())
                .findFirst().get();

        expirationInDays = days;
    }

    public int getExpirationInDays()
    {
        return this.expirationInDays;
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
