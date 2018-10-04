package io.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
import io.yetanotherwhatever.ocpv2.CodeUploadedNotifier;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CodeUploadedHandlerTest {



    S3EventNotification.S3EventNotificationRecord buildGoodRecord(String bucket, String key)
    {
        S3EventNotification.S3EventNotificationRecord record = mock(S3EventNotification.S3EventNotificationRecord.class, Mockito.RETURNS_DEEP_STUBS);
        when(record.getS3().getBucket().getName()).thenReturn(bucket);
        when(record.getS3().getObject().getKey()).thenReturn(key);

        return record;
    }

    S3Event buildS3Event(String bucket, String key)
    {
        S3EventNotification.S3EventNotificationRecord record = buildGoodRecord(bucket, key);
        ArrayList<S3EventNotification.S3EventNotificationRecord> recordList = new ArrayList<>();
        recordList.add(record);

        S3Event s3Event = mock(S3Event.class);
        when(s3Event.getRecords()).thenReturn(recordList);

        return s3Event;
    }

    @Test
    public void parseRecord_normalS3Record_success() throws IOException
    {
        //prepare
        String key = "uploads/code/invitationId/filename.zip";
        S3Event s3Event = buildS3Event("bucket", key);

        CodeUploadedNotifier un = mock(CodeUploadedNotifier.class);
        doAnswer((i) -> { return null;} ).when(un).notifyManager(anyString(), anyString());

        CodeUploadedHandler handler = new CodeUploadedHandler();
        handler.setCodeUploadedNotifier(un);

        //test
        handler.handleRequest(s3Event, null);

        //verify
        verify(un).notifyManager("invitationId", "https://s3.amazonaws.com/bucket/uploads/code/invitationId/filename.zip");
    }
}
