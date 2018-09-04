package com.yetanotherwhatever.ocpv2.lambdas;

import java.io.IOException;

/**
 * Created by achang on 9/3/2018.
 */
public class Inviter {

    private IOcpV2DB db;

    public Inviter(IOcpV2DB db)
    {
        this.db = db;
    }

    //null means valid
    //String return val describes error
    static String isValid(Invitation invite)
    {
        //TODO

        if (!invite.getManagerEmail().endsWith("@symantec.com"))
        {
            return "Manager email must come from symantec.com";
        }

        return null;
    }

    public String invite(Invitation invite)
    {

        try {

            //save invitation to DB
            db.write(invite);

            //generate temporary problem page
            CodingProblemPage problem = new CodingProblemPage();
            problem.generate();

            //notify candidate and manager
            emailCandidate(invite, problem);
            emailManager(invite, problem);

        }
        catch (IOException e)
        {
            //roll back if failed?

            return "Error: " + e.getMessage();
        }


        return "Success";
    }

    private void emailCandidate(Invitation invite, CodingProblemPage problem)
    {
        String emailSub = "";
        String  emailBody = "Thank you for your interest in Symantec.\n\n" +
                "Here is your unique link to the online coding problem: " +
                problem.getUrl();


    }

    private void emailManager(Invitation invite, CodingProblemPage problem)
    {
        //notify manager
        String mgrSub =  "New coding problem registration: " +
                invite.getFirst() + " " +
                invite.getLast()+ ", " +
                invite.getEmail();
        String mgrBody = "Candidate link: " + problem.getUrl();


    }


}
