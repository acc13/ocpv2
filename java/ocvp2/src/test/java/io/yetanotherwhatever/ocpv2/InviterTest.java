package io.yetanotherwhatever.ocpv2;

import io.yetanotherwhatever.ocpv2.aws.S3CodingProblemBuilderBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class InviterTest {

    Inviter inviter;
    Invitation invite;
    IOcpV2DB mockDb;
    IEmailer mockEmailer;
    ICodingProblemBuilder mockCodingProblemBuilder;
    CodingProblem cp;

    private Inviter buildInviter() throws IOException
    {
        mockDb = mock(IOcpV2DB.class);
        mockEmailer = mock(IEmailer.class);
        mockCodingProblemBuilder = mock(S3CodingProblemBuilderBuilder.class);

        cp =  new CodingProblem();
        cp.setLandingPageUrl("http://fake.landingpage.somewhere.com");
        cp.setName("fakeProblemKey");
        cp.setGuid("fakeProblemGuid");
        when(mockCodingProblemBuilder.buildCodingProblem()).thenReturn(cp);

        inviter = new Inviter()
                .setDB(mockDb)
                .setEmailer(mockEmailer)
                .setCodingProblemBuilder(mockCodingProblemBuilder);

        return inviter;
    }

    private Invitation buildGoodInvitation()
    {
        invite = new Invitation();
        invite.setCandidateEmail("First")
                .setCandidateFirstName("Last")
                .setCandidateLastName("candidate@candidate.com")
                .setManagerEmail("manager@symantec.com");

        return invite;
    }

    @Test (expected = IllegalArgumentException.class)
    public void sendInvitation_invalidInput_invalidArgExceptionThrown() throws IOException
    {
        buildInviter();
        buildGoodInvitation();
        invite.setManagerEmail("foo");
        inviter.sendInvitation(invite);
        Assert.fail();


    }

    @Test
    public void sendInvitation_goodInputs_writeToDB() throws IOException
    {
        buildInviter();
        buildGoodInvitation();
        inviter.sendInvitation(invite);
        verify(mockDb).write(any(CandidateRegistration.class));
    }

    @Test
    public void sendInvitation_goodInputs_generateCodingProblem() throws IOException
    {
        buildInviter();
        buildGoodInvitation();
        inviter.sendInvitation(invite);
        verify(mockCodingProblemBuilder).buildCodingProblem();
    }

    @Test
    public void sendInvitation_goodInputs_send2Emails() throws IOException
    {
        buildInviter();
        buildGoodInvitation();
        inviter.sendInvitation(invite);
        verify(mockEmailer, times(2)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void sendInvitation_goodInputs_candidateEmailContainsProblemLandingPageURL() throws IOException
    {
        buildInviter();
        buildGoodInvitation();

        inviter.sendInvitation(invite);

        ArgumentCaptor<String> emailBodyTextCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockEmailer, times(2)).sendEmail(anyString(), anyString(), emailBodyTextCaptor.capture());
        List<String> capturedEmailBodies = emailBodyTextCaptor.getAllValues();
        assertThat(capturedEmailBodies.get(0),  //candidate email sent first
                containsString(cp.getLandingPageUrl()));
    }

    @Test
    public void sendInvitation_goodInputs_managerEmailContainsCandidateDetails() throws IOException
    {
        buildInviter();
        buildGoodInvitation();

        inviter.sendInvitation(invite);

        ArgumentCaptor<String> emailBodyTextCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> emailSubjectCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockEmailer, times(2)).sendEmail(anyString(), emailSubjectCaptor.capture(), emailBodyTextCaptor.capture());

        //check candidate details
        List<String> capturedEmailSubjects = emailSubjectCaptor.getAllValues();
        String managerEmailSubject = capturedEmailSubjects.get(1); //manager email sent second
        assertThat(managerEmailSubject, containsString(invite.getCandidateFirstName()));
        assertThat(managerEmailSubject, containsString(invite.getCandidateLastName()));
        assertThat(managerEmailSubject, containsString(invite.getCandidateEmail()));
    }

    @Test
    public void sendInvitation_goodInputs_managerEmailContainsProblemDetails() throws IOException
    {
        buildInviter();
        buildGoodInvitation();

        inviter.sendInvitation(invite);

        ArgumentCaptor<String> emailBodyTextCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockEmailer, times(2)).sendEmail(anyString(), anyString(), emailBodyTextCaptor.capture());
        List<String> capturedEmailBodies = emailBodyTextCaptor.getAllValues();
        String managerEmailBody = capturedEmailBodies.get(1); //manager email sent second
        assertThat(managerEmailBody,
                containsString(cp.getLandingPageUrl()));
        assertThat(managerEmailBody,
                containsString(cp.getName()));
        assertThat(managerEmailBody,
                containsString(cp.getGuid()));
    }

    @Test
    public void sendInvitation_goodInputs_problemKeyAndLandingPageURLSavedToDB() throws IOException
    {
        buildInviter();
        buildGoodInvitation();

        inviter.sendInvitation(invite);

        ArgumentCaptor<CandidateRegistration> crCaptor = ArgumentCaptor.forClass(CandidateRegistration.class);
        verify(mockDb).write(crCaptor.capture());
        CandidateRegistration candidateRegistrationArg = crCaptor.getValue();
        assertThat(candidateRegistrationArg.getCodingProblem().getLandingPageUrl(),
                is(equalTo((cp.getLandingPageUrl()))));
        assertThat(candidateRegistrationArg.getCodingProblem().getName(),
                is(equalTo(cp.getName())));
        assertThat(candidateRegistrationArg.getCodingProblem().getGuid(),
                is(equalTo(cp.getGuid())));
    }
}
