package com.yetanotherwhatever.ocpv2.lambdas;

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

    public void sendInvitation(Invitation invite) throws IllegalArgumentException, IOException
    {
        if(null == db || null == codingProblem || null == emailer)
        {
            throw new IllegalStateException("Inviter not initialized.");
        }

        invite.validate();
        logger.info("Invitation validated.");

        codingProblem.setup();
        logger.info("Coding problem page set up complete.");

        invite.setProblemGuid(codingProblem.getProblemGuid());
        invite.setProblemKey(codingProblem.getProblemKey());
        invite.setProblemLandingPageURL(codingProblem.getLandingPageURL());
        db.write(invite);
        logger.info("Invite saved to DB.");


        emailCandidate(invite.getCandidateEmail(), codingProblem.getLandingPageURL());
        logger.info("Candidate email sent.");
        emailManager(invite, codingProblem);
        logger.info("Manager email sent.");

    }

    static final String NL = System.getProperty("line.separator");
    private void emailCandidate(String destEmailAddress, String url) throws IOException
    {
        String emailSubject = "Welcome to the Symantec online coding problem";
        String  emailBody = "Thank you for your interest in Symantec!" + NL +
                "Here is your unique link to the online coding problem: " + url;

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
                "Problem key: " + problem.getProblemKey() + NL +
                "Problem GUID: " + problem.getProblemGuid() +NL +
                "Candidate link: " + problem.getLandingPageURL();

        emailer.sendEmail(invite.getManagerEmail(), emailSubject, emailBody);
    }


}
