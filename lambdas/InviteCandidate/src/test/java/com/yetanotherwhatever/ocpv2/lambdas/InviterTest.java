package com.yetanotherwhatever.ocpv2.lambdas;

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
    EmailHelper mockEmailer;

    private void buildInviter()
    {
        inviter = new Inviter();
        inviter.setDB(mockDb).setEmailer(mockEmailer);
    }

    private void buildGoodInvitation()
    {
        invite = new Invitation();
        invite.setCandidateEmail("First")
                .setCandidateFirstName("Last")
                .setCandidateLastName("candidate@candidate.com")
                .setManagerEmail("manager@symantec.com");
    }

    @Test
    public void sendInvitation_goodInputs_writeToDB() throws IOException
    {
        buildInviter();
        buildGoodInvitation();
        inviter.sendInvitation(invite);
        verify(mockDb).write(ArgumentMatchers.eq(invite));
    }


    public void sendInvitation_goodInputs_sendEmails() throws IOException
    {
        buildInviter();
        IOcpV2DB db = mock(IOcpV2DB.class);
        inviter.setDB(db);
        buildGoodInvitation();
        inviter.sendInvitation(invite);
        verify(mockEmailer, times(2)).sendEmail(anyString(), anyString(), anyString());
    }

}
