package com.yetanotherwhatever.ocpv2.lambdas;

import com.amazonaws.services.kinesis.model.InvalidArgumentException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class InviterTest {

    Inviter inviter;
    Invitation invite;
    IOcpV2DB mockDb;
    IEmailer mockEmailer;
    ICodingProblem mockCodingProblem;

    private Inviter buildInviter()
    {
        mockDb = mock(IOcpV2DB.class);
        mockEmailer = mock(IEmailer.class);
        mockCodingProblem = mock(S3CodingProblem.class);

        String landingPageURL = "http://fake.landingpage.somewhere.com";
        String problemKey = "fakeProblemKey";
        String problemGuid= "fakeProblemGuid";
        when(mockCodingProblem.getLandingPageURL()).thenReturn(landingPageURL);
        when(mockCodingProblem.getProblemKey()).thenReturn(problemKey);
        when(mockCodingProblem.getProblemGuid()).thenReturn(problemGuid);

        inviter = new Inviter()
                .setDB(mockDb)
                .setEmailer(mockEmailer)
                .setCodingProblem(mockCodingProblem);

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

    @Test (expected = InvalidArgumentException.class)
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
        verify(mockDb).write(ArgumentMatchers.eq(invite));
    }

    @Test
    public void sendInvitation_goodInputs_generateCodingProblem() throws IOException
    {
        buildInviter();
        buildGoodInvitation();
        inviter.sendInvitation(invite);
        verify(mockCodingProblem).setup();
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
                containsString(mockCodingProblem.getLandingPageURL()));
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
                containsString(mockCodingProblem.getLandingPageURL()));
        assertThat(managerEmailBody,
                containsString(mockCodingProblem.getProblemKey()));
        assertThat(managerEmailBody,
                containsString(mockCodingProblem.getProblemGuid()));
    }

    @Test
    public void sendInvitation_goodInputs_problemKeyAndLandingPageURLSavedToDB() throws IOException
    {
        buildInviter();
        buildGoodInvitation();

        inviter.sendInvitation(invite);

        ArgumentCaptor<Invitation> inviteCaptor = ArgumentCaptor.forClass(Invitation.class);
        verify(mockDb).write(inviteCaptor.capture());
        Invitation inviteArg = inviteCaptor.getValue();
        assertThat(inviteArg.getProblemLandingPageURL(),
                is(equalTo((mockCodingProblem.getLandingPageURL()))));
        assertThat(inviteArg.getProblemKey(),
                is(equalTo(mockCodingProblem.getProblemKey())));
        assertThat(inviteArg.getProblemGuid(),
                is(equalTo(mockCodingProblem.getProblemGuid())));
    }
}
