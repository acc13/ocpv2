package com.yetanotherwhatever.ocpv2.lambdas;

import com.amazonaws.services.kinesis.model.InvalidArgumentException;

import java.io.IOException;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by achang on 9/3/2018.
 */
public class Inviter {

    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(Inviter.class);


    private IOcpV2DB db;
    private IEmailer emailer;
    private ICodingProblem codingProblem;

    public Inviter(){}

    protected Inviter setDB(IOcpV2DB db)
    {
        this.db = db;
        return this;
    }

    protected Inviter setEmailer(IEmailer emailer)
    {
        this.emailer = emailer;
        return this;
    }

    protected Inviter setCodingProblem(ICodingProblem codingProblem)
    {
        this.codingProblem = codingProblem;
        return this;
    }

    public void sendInvitation(Invitation invite) throws InvalidArgumentException, IOException
    {
        if(null == db || null == codingProblem || null == emailer)
        {
            throw new IllegalStateException("Inviter not initialized.");
        }

        invite.validate();

        codingProblem.setup();

        invite.setProblemGuid(codingProblem.getProblemGuid());
        invite.setProblemKey(codingProblem.getProblemKey());
        invite.setProblemLandingPageURL(codingProblem.getLandingPageURL());
        db.write(invite);


        emailCandidate(invite.getCandidateEmail(), codingProblem.getLandingPageURL());
        emailManager(invite, codingProblem);

    }

    private void emailCandidate(String destEmailAddress, String url) throws IOException
    {
        String emailSubject = "";
        String  emailBody = "Thank you for your interest in Symantec.\n\n" +
                "Here is your unique link to the online coding problem: " +
                url;

        emailer.sendEmail(destEmailAddress, emailSubject, emailBody);
    }

    private void emailManager(Invitation invite, ICodingProblem problem) throws IOException
    {
        //notify manager
        String emailSubject =  "New coding problem registration: " +
                invite.getCandidateFirstName() + " " +
                invite.getCandidateLastName()+ ", " +
                invite.getCandidateEmail();
        String emailBody =
                "Problem key: " + problem.getProblemKey() + "\n\n" +
                "Problem GUID: " + problem.getProblemGuid() + "\n\n" +
                "Candidate link: " + problem.getLandingPageURL();

        emailer.sendEmail(invite.getManagerEmail(), emailSubject, emailBody);
    }


}
