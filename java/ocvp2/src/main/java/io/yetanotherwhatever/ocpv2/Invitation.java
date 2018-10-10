package io.yetanotherwhatever.ocpv2;

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
    private String invitationDate;

    public Invitation ()
    {
        invitationDate = Utils.formatDateISO8601(new Date());
    }

    public void setInvitationDate(String invitationDate)
    {
        this.invitationDate = invitationDate;
    }

    public String getInvitationDate()
    {
        return invitationDate;
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

    public void validate() throws IllegalArgumentException
    {
        isValidName(getCandidateFirstName());
        isValidName(getCandidateLastName());

        isValidEmail(getCandidateEmail());
        isValidManagerEmail(getManagerEmail());
    }


    protected static void isValidName(String name) throws IllegalArgumentException
    {
        //just check length for now
        if (name == null || name.length() == 0 || name.length() > 100)
        {
            throw new IllegalArgumentException("Name of invalid length: '" + name + "'");
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

    protected static void isValidEmail(String email) throws IllegalArgumentException
    {
        //just check length for now
        if (email == null || email.length() == 0 || email.length() > 100)
        {
            throw new IllegalArgumentException("Email of invalid length: '" + email + "'");
        }
    }

    protected static void isValidManagerEmail(String email)
    {
        //TODO
        //manager whitelist - subscribed to online coding problem portal

        if (!email.endsWith("@symantec.com"))
        {
            throw new IllegalArgumentException("Manager must have a symantec.com email address");
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
