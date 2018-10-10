package io.yetanotherwhatever.ocpv2;

public class ProblemPage {

    private String problemKey;
    private String problemGuid;
    private String problemLandingPageURL;

    //tracking
    private String succeeded = "never";
    private int attempts = 0;



    public String getProblemGuid() {
        return problemGuid;
    }

    public void setProblemGuid(String problemGuid) {
        this.problemGuid = problemGuid;
    }

    public String getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(String succeeded) {
        this.succeeded = succeeded;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
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
}
