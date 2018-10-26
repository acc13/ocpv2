package io.yetanotherwhatever.ocpv2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Created by achang on 9/3/2018.
 */


public class Invitation {

    public enum Type
    {
        FULL_TIME(0), INTERN(1);

        private int _value;

        Type(int Value) {
            this._value = Value;
        }

        public int getValue() {
            return _value;
        }

        public static Type fromInt(int i) {
            for (Type b : Type .values()) {
                if (b.getValue() == i) { return b; }
            }
            return null;
        }
    }

    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(Invitation.class);

    private String candidateFirstName;
    private String candidateLastName;
    private String candidateEmail;
    private String managerEmail;
    private String invitationDate;
    private Type type;
    private String resumeUrl;

    public Invitation ()
    {
        invitationDate = Utils.formatDateISO8601(new Date());
        this.type = Type.FULL_TIME;
    }

    public Invitation (Type type)
    {
        this();
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public void validate() throws IllegalArgumentException
    {
        isValidName(getCandidateFirstName());
        isValidName(getCandidateLastName());

        isValidEmail(getCandidateEmail());
        isValidManagerEmail(getManagerEmail());

        if(type == Type.INTERN)
        {
            isValidInternEmail(getCandidateEmail());
        }
    }

    public void isValidInternEmail(String candidateEmail) throws IllegalArgumentException
    {
        if (!candidateEmail.endsWith(".edu"))
        {
            throw new IllegalArgumentException("Intern registered with non .edu email address: '" + candidateEmail + "'");
        }
    }


    protected void isValidName(String name) throws IllegalArgumentException
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
    protected boolean isValidNameWarningOnly(String name)
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

    protected void isValidEmail(String email) throws IllegalArgumentException
    {
        //just check length for now
        if (email == null || email.length() == 0 || email.length() > 100)
        {
            throw new IllegalArgumentException("Email of invalid length: '" + email + "'");
        }
    }

    protected void isValidManagerEmail(String email)
    {
        //TODO
        //manager whitelist - subscribed to online coding problem portal

        if (!email.endsWith("@symantec.com"))
        {
            throw new IllegalArgumentException("Manager must have a symantec.com email address");
        }

        isValidManagerEmailWarningOnly(email);
    }

    protected boolean isValidManagerEmailWarningOnly(String managerEmail){

        final String whitelistPattern = "[a-zA-Z0-9_]+@symantec.com";
        if (!managerEmail.matches(whitelistPattern)){

            logger.error("Unexpected manager email encountered:" + managerEmail);
            logger.error("Manager email does not match valid regex pattern:" + whitelistPattern);

            return false;
        }

        return true;
    }
}
