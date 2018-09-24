package com.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.s3.event.S3EventNotification;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by achang on 9/18/2018.
 */
public class OutputUploadedHandlerTest {

    @Test
    public void parseRecord_wellFormedKey_parsedAsExpected()
    {
        S3EventNotification.S3EventNotificationRecord stubRecord =
                mock(S3EventNotification.S3EventNotificationRecord.class);
        S3EventNotification.S3Entity stubS3 = mock(S3EventNotification.S3Entity.class);
        when(stubRecord.getS3()).thenReturn(stubS3);
        S3EventNotification.S3BucketEntity stubBucket = mock(S3EventNotification.S3BucketEntity.class);
        when(stubS3.getBucket()).thenReturn(stubBucket);
        when(stubBucket.getName()).thenReturn("bucketname");
        S3EventNotification.S3ObjectEntity  stubS3Object = mock(S3EventNotification.S3ObjectEntity.class);
        when(stubS3.getObject()).thenReturn(stubS3Object);
        when(stubS3Object.getKey()).thenReturn("uploads/output/invitation_id/problem_name/upload_id.txt");

        OutputUploadedHandler  mockOUH = mock(OutputUploadedHandler.class);
        when(mockOUH.getS3WebBucketName()).thenReturn("s3WebBucketName");
        when(mockOUH.parseRecord(stubRecord)).thenCallRealMethod();

        mockOUH.parseRecord(stubRecord);

        assertThat(mockOUH.testFileName, is(equalTo("bucketname:uploads/output/invitation_id/problem_name/upload_id.txt")));
        assertThat(mockOUH.invitationId, is(equalTo("invitation_id")));
        assertThat(mockOUH.expectedFileName, is(equalTo("s3WebBucketName:expectedOutputs/problem_name-out.txt")));
        assertThat(mockOUH.uploadId, is(equalTo("upload_id")));

    }
}

