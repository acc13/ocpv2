package io.yetanotherwhatever.ocpv2.aws;

import io.yetanotherwhatever.ocpv2.*;
import io.yetanotherwhatever.ocpv2.fakes.FakeS3CodingProblemBuilder;
import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.atLeast;

/**
 * Created by achang on 10/17/2018.
 */
public class LambdaHandlerInviteCandidateTest {

    private Invitation buildValidFulltimeInvitation()
    {
        Invitation i = spy(Invitation.class);
        i.setCandidateFirstName("Fulltime");
        i.setCandidateLastName("Candidate");
        i.setCandidateEmail("candidate@fulltime.com");
        i.setManagerEmail("manager@symantec.com");
        i.setType(Invitation.Type.FULL_TIME);

        return i;
    }

    private Invitation buildInvalidFulltimeInvitation()
    {
        Invitation i = spy(Invitation.class);
        i.setCandidateFirstName("Fulltime");
        i.setCandidateLastName("Candidate");
        i.setCandidateEmail("candidate@fulltime.com");
        i.setManagerEmail("invalidemail");
        i.setType(Invitation.Type.FULL_TIME);

        return i;
    }

    @Test
    public void handleRequest_validInvitation_validated()
    {
        Invitation invite = buildValidFulltimeInvitation();

        ICodingProblemBuilder builder = new FakeS3CodingProblemBuilder();
        IOcpV2DB db = mock(DynamoOcpV2DB.class);
        IEmailer emailer = mock(SESEmailHelper.class);

        LambdaHandlerInviteCandidate lambda =
                new LambdaHandlerInviteCandidate(db, builder, emailer);

        lambda.handleRequest(invite, null);

        verify(invite, atLeast(1)).validate();
    }

    @Test
    public void handleRequest_validInvitation_problemBuilt() throws IOException
    {
        Invitation i = buildValidFulltimeInvitation();

        ICodingProblemBuilder builder = spy(FakeS3CodingProblemBuilder.class);
        IOcpV2DB db = mock(DynamoOcpV2DB.class);
        IEmailer emailer = mock(SESEmailHelper.class);

        LambdaHandlerInviteCandidate lambda =
                new LambdaHandlerInviteCandidate(db, builder, emailer);

        lambda.handleRequest(i, null);

        verify(builder, atLeast(1)).buildCodingProblem();
    }

    @Test
    public void handleRequest_validInvitation_candidateEmailed() throws IOException
    {
        Invitation i = buildValidFulltimeInvitation();

        FakeS3CodingProblemBuilder builder = spy(FakeS3CodingProblemBuilder.class);
        IOcpV2DB db = mock(DynamoOcpV2DB.class);
        IEmailer emailer = mock(SESEmailHelper.class);

        LambdaHandlerInviteCandidate lambda =
                new LambdaHandlerInviteCandidate(db, builder, emailer);

        lambda.handleRequest(i, null);

        ArgumentCaptor<String> emailText = ArgumentCaptor.forClass(String.class);
        verify(emailer, atLeast(1)).sendEmail(eq(i.getCandidateEmail()), anyString(), emailText.capture());
        List<String> capturedEmailBodies = emailText.getAllValues();
        assertThat(capturedEmailBodies.get(0),
                containsString(builder.getLastCodingProblemBuilt().getLandingPageUrl()));
    }

    @Test
    public void handleRequest_validInvitation_managerEmailed() throws IOException
    {
        Invitation invite = buildValidFulltimeInvitation();

        FakeS3CodingProblemBuilder builder = spy(FakeS3CodingProblemBuilder.class);
        IOcpV2DB db = mock(DynamoOcpV2DB.class);
        IEmailer emailer = mock(SESEmailHelper.class);

        LambdaHandlerInviteCandidate lambda =
                new LambdaHandlerInviteCandidate(db, builder, emailer);

        lambda.handleRequest(invite, null);

        ArgumentCaptor<String> emailBodyTextCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> emailSubjectCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailer, times(2)).sendEmail(anyString(), emailSubjectCaptor.capture(), emailBodyTextCaptor.capture());

        //check email contains candidate details
        List<String> capturedEmailSubjects = emailSubjectCaptor.getAllValues();
        String managerEmailSubject = capturedEmailSubjects.get(1); //manager email sent second
        assertThat(managerEmailSubject, StringContains.containsString(invite.getCandidateFirstName()));
        assertThat(managerEmailSubject, StringContains.containsString(invite.getCandidateLastName()));
        assertThat(managerEmailSubject, StringContains.containsString(invite.getCandidateEmail()));

        //check email contains problem details
        CodingProblem cp = builder.getLastCodingProblemBuilt();
        List<String> capturedEmailBodies = emailBodyTextCaptor.getAllValues();
        String managerEmailBody = capturedEmailBodies.get(1); //manager email sent second
        assertThat(managerEmailBody,
                StringContains.containsString(cp.getLandingPageUrl()));
        assertThat(managerEmailBody,
                StringContains.containsString(cp.getName()));
        assertThat(managerEmailBody,
                StringContains.containsString(cp.getGuid()));
    }

    @Test
    public void handleRequest_invalidInvitation_400()
    {
        Invitation invite = buildInvalidFulltimeInvitation();

        ICodingProblemBuilder builder = new FakeS3CodingProblemBuilder();
        IOcpV2DB db = mock(DynamoOcpV2DB.class);
        IEmailer emailer = mock(SESEmailHelper.class);

        LambdaHandlerInviteCandidate lambda =
                new LambdaHandlerInviteCandidate(db, builder, emailer);

        lambda.handleRequest(invite, null);

        assertThat(lambda.getLastResponseJson().get("statusCode"), is(equalTo(400)));
    }
}
