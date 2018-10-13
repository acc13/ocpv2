package io.yetanotherwhatever.ocpv2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by achang on 9/3/2018.
 */
public class Inviter {

    final String PROBLEM_EXPIRATION_ENV_VAR = "PROBLEM_EXPIRY_IN_DAYS";

    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(Inviter.class);


    private IOcpV2DB db;
    private IEmailer emailer;
    private ICodingProblemBuilder codingProblemBuilder;

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

    public Inviter setCodingProblemBuilder(ICodingProblemBuilder codingProblemBuilder)
    {
        this.codingProblemBuilder = codingProblemBuilder;
        return this;
    }

    public void sendInvitation(Invitation invite) throws IllegalArgumentException, IOException
    {
        if(null == db || null == codingProblemBuilder || null == emailer)
        {
            throw new IllegalStateException("Inviter not initialized.");
        }

        invite.validate();
        logger.info("Invitation validated.");

        CodingProblem cp = codingProblemBuilder.buildCodingProblem();
        logger.info("Coding problem page set up complete.");

        CandidateWorkflow workflow = new CandidateWorkflow(invite, cp);

        db.write(workflow);
        logger.info("Invite saved to DB.");


        emailCandidate(invite.getCandidateEmail(), cp.getLandingPageUrl(), invite.getManagerEmail());
        logger.info("Candidate email sent.");
        emailManager(invite, cp);
        logger.info("Manager email sent.");

    }

    static final String NL = "<br>" + System.getProperty("line.separator");
    private void emailCandidate(String destEmailAddress, String url, String managerEmail) throws IOException
    {
        final String techSupport = "andrew_chang@symantec.com";
        String emailSubject = "Welcome to the Symantec online coding problem";
        String  emailBody = "Thank you for your interest in Symantec!<br>" + NL +
                "Here is your unique link to the online coding problem: <a href='" + url + "'>" + url + "</a>" + NL + NL +
                "<b>WARNING: You will have " + codingProblemBuilder.getExpirationInDays() + " days to complete the coding problem, before your personalized link expires.</b>" + NL + NL +
                "For recruiting questions, you can email your hiring manager here: <a href='mailto:" + managerEmail + "'>" + managerEmail + "</a>." + NL +
                "For any technical issues, please email <a href='mailto:" + techSupport + "'>" + techSupport + "</a>.";

        emailer.sendEmail(destEmailAddress, emailSubject, emailBody);
    }

    private void emailManager(Invitation invite, CodingProblem problem) throws IOException
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
                "Candidate resume: " + (null != invite.getResumeUrl() && invite.getResumeUrl().length() > 0?
                                "<a href='" + invite.getResumeUrl() + "'>" + invite.getResumeUrl() + "</a>" :
                                "Not available.") + NL +
                "Problem key: " + problem.getName() + NL +
                "Problem GUID: " + problem.getGuid() + NL +
                "Candidate link: <a href='" + problem.getLandingPageUrl() + "'>" + problem.getLandingPageUrl() + "</a>" +  NL +
                "Link expires: " + calcExpiry();

        emailer.sendEmail(invite.getManagerEmail(), emailSubject, emailBody);
    }

    private String calcExpiry()
    {
        Calendar expiry = Calendar.getInstance();

        try {
            int days = codingProblemBuilder.getExpirationInDays();

            expiry.add(Calendar.DAY_OF_YEAR, days);
            Date date = expiry.getTime();
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
            return format.format(date);
        }
        catch(NumberFormatException e)
        {
            //not fatal
            logger.warn("Environment variable not set: " + PROBLEM_EXPIRATION_ENV_VAR);
            return "Error: configuration not found.";
        }
    }


}
