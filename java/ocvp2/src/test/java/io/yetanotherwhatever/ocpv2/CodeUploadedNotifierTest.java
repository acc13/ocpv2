package io.yetanotherwhatever.ocpv2;

import io.yetanotherwhatever.ocpv2.aws.DynamoOcpV2DB;
import io.yetanotherwhatever.ocpv2.aws.S3FileStoreImpl;
import io.yetanotherwhatever.ocpv2.aws.SESEmailHelper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class CodeUploadedNotifierTest {

    @Test
    public void notifyManager_validInputs_buildSubjectCalled() throws IOException
    {
        CodeUploadedNotifier cn = mock(CodeUploadedNotifier.class);

        when(cn.setDb(any())).thenCallRealMethod();
        when(cn.setEmailer(any())).thenCallRealMethod();
        when(cn.setFileStore(any())).thenCallRealMethod();

        IOcpV2DB db = mock(DynamoOcpV2DB.class);
        cn.setDb(db);
        IEmailer emailer = mock(SESEmailHelper.class);
        cn.setEmailer(emailer);
        IFileStore fs = mock(S3FileStoreImpl.class);
        cn.setFileStore(fs);

        Invitation i = new Invitation().setManagerEmail("manager@email.com");
        CandidateWorkflow wf = new CandidateWorkflow(null, null).setInvitation(i);
        when(db.getWorkflow(anyString())).thenReturn(wf);

        when(emailer.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        when(cn.buildEmailBody(any(), anyString())).thenReturn("");
        when(cn.notifyManager(anyString(), anyString())).thenCallRealMethod();

        cn.notifyManager("inviationid", "https://foo.bar/download");

        verify(cn, times(1)).buildEmailSubject(any());
    }

    @Test
    public void buildEmailSubject_validInputs_includesFirstLastEmail() throws IOException
    {
        Invitation i = new Invitation()
                .setCandidateFirstName("first")
                .setCandidateLastName("last")
                .setCandidateEmail("foo@bar.com");
        CodingProblem cp = new CodingProblem();
        CandidateWorkflow cw = new CandidateWorkflow(i, cp);

        CodeUploadedNotifier cn = new CodeUploadedNotifier();
        assertThat(cn.buildEmailSubject(cw), containsString("first"));
        assertThat(cn.buildEmailSubject(cw), containsString("last"));
        assertThat(cn.buildEmailSubject(cw), containsString("foo@bar.com"));
    }
}
