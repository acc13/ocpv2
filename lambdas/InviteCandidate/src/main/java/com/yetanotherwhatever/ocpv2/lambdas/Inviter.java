package com.yetanotherwhatever.ocpv2.lambdas;

import com.amazonaws.services.kinesis.model.InvalidArgumentException;

import java.io.IOException;

/**
 * Created by achang on 9/3/2018.
 */
public class Inviter {

    private IOcpV2DB db;
    private IEmailer emailer;

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

    //null means valid
    //String return val describes error
    public static String validate(Invitation invite) throws InvalidArgumentException
    {
        //TODO

        if (!invite.getManagerEmail().endsWith("@symantec.com"))
        {
            throw new InvalidArgumentException("Manager must have a symantec.com email address");
        }

        return null;
    }

    public void sendInvitation(Invitation invite) throws InvalidArgumentException, IOException
    {
        validate(invite);

        if (db != null) {
            db.write(invite);
        }

        CodingProblemPage problem = new CodingProblemPage();
        problem.generate();

        if (null != emailer) {
            emailCandidate(invite.getCandidateEmail(), problem.getUrl());
            emailManager(invite, problem);
        }
    }

    private void emailCandidate(String destEmailAddress, String url)
    {
        String emailSubject = "";
        String  emailBody = "Thank you for your interest in Symantec.\n\n" +
                "Here is your unique link to the online coding problem: " +
                url;

        emailer.sendEmail(destEmailAddress, emailSubject, emailBody);
    }

    private void emailManager(Invitation invite, CodingProblemPage problem)
    {
        //notify manager
        String emailSubject =  "New coding problem registration: " +
                invite.getCandidateFirstName() + " " +
                invite.getCandidateLastName()+ ", " +
                invite.getCandidateEmail();
        String emailBody = "Candidate link: " + problem.getUrl();

        emailer.sendEmail(invite.getManagerEmail(), emailSubject, emailBody);
    }


}
