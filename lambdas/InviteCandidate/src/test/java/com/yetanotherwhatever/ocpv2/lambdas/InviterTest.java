package com.yetanotherwhatever.ocpv2.lambdas;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class InviterTest {

    Inviter inviter;
    Invitation invite;
    IOcpV2DB mockDb;
    IEmailer mockEmailer;
    CodingProblem mockCodingProblem;

    private Inviter buildInviter()
    {
        mockDb = mock(IOcpV2DB.class);
        mockEmailer = mock(IEmailer.class);
        mockCodingProblem = mock(CodingProblem.class);
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

    @Test
    public void sendInvitation_goodInputs_writeToDB() throws IOException
    {
        buildInviter();
        buildGoodInvitation();
        inviter.sendInvitation(invite);
        verify(mockDb).write(ArgumentMatchers.eq(invite));
    }

    @Test
    public void sendInvitation_goodInputs_sendEmails() throws IOException
    {
        buildInviter();
        buildGoodInvitation();
        inviter.sendInvitation(invite);
        verify(mockEmailer, times(2)).sendEmail(anyString(), anyString(), anyString());
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
    public void sendInvitation_invalidInput_invalidArgExceptionThrown() throws IOException
    {
        buildInviter();
        buildGoodInvitation();
        invite.setManagerEmail("foo");
        try {
            inviter.sendInvitation(invite);
            Assert.fail();
        }
        catch (IOException e)
        {
            //pass
        }


    }

}
