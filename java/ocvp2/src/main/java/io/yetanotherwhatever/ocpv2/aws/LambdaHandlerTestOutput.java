package io.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;

import io.yetanotherwhatever.ocpv2.OutputTester;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by achang on 9/12/2018.
 */
public class LambdaHandlerTestOutput implements RequestHandler<S3Event,S3Event> {

    String invitationId, testFileName, expectedFileName, uploadId;

    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(LambdaHandlerTestOutput.class);

    public S3Event handleRequest(S3Event s3Event, Context context) {

        // For each record.
        for (S3EventNotification.S3EventNotificationRecord record : s3Event.getRecords()) {
            try {

                OutputTester oc = new OutputTester();
                oc.setDB(new DynamoOcpV2DB());
                oc.setOutputStore(new S3FileStoreImpl());

                parseRecord(record);

                oc.checkOutput(invitationId, uploadId, testFileName , expectedFileName);

                logger.debug("Output check complete.");

            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        return s3Event;
    }

    //this is for mocking
    String getS3WebBucketName()
    {
        return S3CodingProblemBuilder.getS3WebBucket();
    }



    boolean parseRecord(S3EventNotification.S3EventNotificationRecord record)
    {
        ///////////////////////////////
        //  EXTRACT TEST FILENAME
        ///////////////////////////////
        String bucketName = record.getS3().getBucket().getName();
        String testKey = record.getS3().getObject().getKey();

        this.testFileName = bucketName + ":" + testKey;

        logger.debug("Test output file created in S3: " + testFileName );


        ///////////////////////////////
        //  EXTRACT INVITATION ID
        ///////////////////////////////
        String[] parts =testKey.split("/");
        //test key should contain uploads/output/<invitation Id>/<problem name>/<random uuid>.txt
        if (parts.length != 5)
        {
            throw new IllegalArgumentException("Malformed output upload key: " + testKey);
        }

        this.invitationId = parts[2];


        ////////////////////////////////////////////////
        //  EXTRACT EXPECTED OUTPUT FILENAME
        ////////////////////////////////////////////////
        String problemName = parts[3];

        //correct output is stored in the S3Web bucket, under the problems/output folder
        //under key <problem-name>-out.txt
        String s3webBucket = getS3WebBucketName();
        this.expectedFileName = s3webBucket + ":" + "expectedOutputs/" + problemName + "-out.txt";

        logger.debug("Correct output file: " + expectedFileName);


        ////////////////////////////////////////////////
        //  EXTRACT UPLOAD ID
        ////////////////////////////////////////////////
        uploadId = parts[4];

        int dot = parts[4].indexOf('.');
        if (dot != -1)
        {
            uploadId = parts[4].substring(0, dot);    //strip extension
        }

        return true;

    }
}
