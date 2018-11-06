package io.yetanotherwhatever.ocpv2.aws;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LambdaHandlerGetInternCandidatesTest extends OutputStream {

    @Test
    public void handleRequest_normalCall_requestsInternListFromDb() throws IOException
    {
        DynamoOcpV2DB db = mock(DynamoOcpV2DB.class);

        LambdaHandlerGetInternCandidates lambdaHander = new LambdaHandlerGetInternCandidates();
        lambdaHander.setDb(db);

        OutputStream outputStream = new LambdaHandlerGetInternCandidatesTest();
        lambdaHander.handleRequest(null, outputStream, null);

        verify(db, times(1)).listAllInterns();
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public void write(int b) throws IOException {

    }
}
