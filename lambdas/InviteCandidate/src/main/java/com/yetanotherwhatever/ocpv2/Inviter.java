package com.yetanotherwhatever.ocpv2;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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

    public Inviter setDB(IOcpV2DB db)
    {
        this.db = db;
        return this;
    }

    public Inviter setEmailer(IEmailer emailer)
    {
        this.emailer = emailer;
        return this;
    }

    public Inviter setCodingProblem(ICodingProblem codingProblem)
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

    static final String NL = "<br>" + System.getProperty("line.separator");
    private void emailCandidate(String destEmailAddress, String url) throws IOException
    {
        String emailSubject = "Welcome to the Symantec online coding problem";
        String  emailBody = "Thank you for your interest in Symantec!<br>" + NL +
                "Here is your unique link to the online coding problem: <a href='" + url + "'>" + url + "</a>" + NL + NL +
                "<b>WARNING: You will have 7 days to complete the coding problem, before your personalized link expires.</b>";

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
                "Candidate first name: " + invite.getCandidateFirstName()  + NL +
                "Candidate last name: " + invite.getCandidateLastName() + NL +
                "Candidate email: " + invite.getCandidateEmail() + NL +
                "Problem key: " + problem.getProblemKey() + NL +
                "Problem GUID: " + problem.getProblemGuid() + NL +
                "Candidate link: <a href='" + problem.getLandingPageURL() + "'>" + problem.getLandingPageURL() + "</a>" +  NL +
                "Link expires: " + calcExpiry();

        emailer.sendEmail(invite.getManagerEmail(), emailSubject, emailBody);
    }

    private String calcExpiry()
    {
        Calendar expiry = Calendar.getInstance();

        try {
            int days = Integer.parseInt(System.getenv("PROBLEM_EXPIRY_IN_DAYS"));
        }
        catch(NumberFormatException e)
        {
            return "Error: configuration not found.";
        }

        expiry.add(Calendar.DAY_OF_YEAR, 7);
        Date date = expiry.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        return format.format(date);
    }


}
