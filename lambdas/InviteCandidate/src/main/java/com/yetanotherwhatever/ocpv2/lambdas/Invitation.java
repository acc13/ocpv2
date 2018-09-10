package com.yetanotherwhatever.ocpv2.lambdas;

import com.amazonaws.services.kinesis.model.InvalidArgumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Created by achang on 9/3/2018.
 */
public class Invitation {

    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(Invitation.class);

    private String candidateFirstName;
    private String candidateLastName;
    private String candidateEmail;
    private String managerEmail;
    private String problemKey;

    public String getProblemGuid() {
        return problemGuid;
    }

    public void setProblemGuid(String problemGuid) {
        this.problemGuid = problemGuid;
    }

    private String problemGuid;
    private String problemLandingPageURL;
    private Date creationDate;


    public Invitation ()
    {

        creationDate = new Date();
    }

    public String getCandidateFirstName() {
        return candidateFirstName;
    }

    public Invitation setCandidateFirstName(String candidateFirstName) {
        this.candidateFirstName = candidateFirstName;

        return this;
    }

    public String getCandidateLastName() {
        return candidateLastName;
    }

    public Invitation setCandidateLastName(String candidateLastName) {
        this.candidateLastName = candidateLastName;

        return this;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public Invitation setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;

        return this;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public Invitation setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;

        return this;
    }

    public String getProblemKey() {
        return problemKey;
    }

    public void setProblemKey(String problemKey) {
        this.problemKey = problemKey;
    }

    public String getProblemLandingPageURL() {
        return problemLandingPageURL;
    }

    public void setProblemLandingPageURL(String problemLandingPageURL) {
        this.problemLandingPageURL = problemLandingPageURL;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void validate() throws InvalidArgumentException
    {
        isValidName(getCandidateFirstName());
        isValidName(getCandidateLastName());

        isValidEmail(getCandidateEmail());
        isValidManagerEmail(getManagerEmail());
    }


    protected static void isValidName(String name) throws InvalidArgumentException
    {
        //just check length for now
        if (name == null || name.length() == 0 || name.length() > 100)
        {
            throw new InvalidArgumentException("Name of invalid length: \"" + name + "\"");
        }

        isValidNameWarningOnly(name);
    }

    //soft fail for this
    //its own method, to make unit testing easier
    protected static boolean isValidNameWarningOnly(String name)
    {
        final String whitelistPattern = "[a-zA-Z\\.\\- \']+";

        if (!name.matches(whitelistPattern ))
        {
            logger.error("Unexpected character encountered in name:" + name);
            logger.error("Does not match valid regex pattern:" + whitelistPattern);
            return false;
        }

        return true;
    }

    protected static void isValidEmail(String email) throws InvalidArgumentException
    {
        //just check length for now
        if (email == null || email.length() == 0 || email.length() > 100)
        {
            throw new InvalidArgumentException("Email of invalid length: \"" + email + "\"");
        }
    }

    protected static void isValidManagerEmail(String email)
    {
        //TODO
        //manager whitelist - subscribed to online coding problem portal

        if (!email.endsWith("@symantec.com"))
        {
            throw new InvalidArgumentException("Manager must have a symantec.com email address");
        }

        isValidManagerEmailWarningOnly(email);
    }

    protected static boolean isValidManagerEmailWarningOnly(String managerEmail){

        final String whitelistPattern = "[a-zA-Z0-9_]+@symantec.com";
        if (!managerEmail.matches(whitelistPattern)){

            logger.error("Unexpected manager email encountered:" + managerEmail);
            logger.error("Manager email does not match valid regex pattern:" + whitelistPattern);

            return false;
        }

        return true;
    }
}
