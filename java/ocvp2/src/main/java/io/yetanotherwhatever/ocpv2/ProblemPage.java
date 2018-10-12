package io.yetanotherwhatever.ocpv2;

public class ProblemPage {

    private String problemKey;
    private String problemGuid;
    private String problemLandingPageURL;


    public String getProblemGuid()
    {
        return problemGuid;
    }

    public void setProblemGuid(String problemGuid)
    {
        this.problemGuid = problemGuid;
    }


    public String getProblemKey()
    {
        return problemKey;
    }

    public void setProblemKey(String problemKey)
    {
        this.problemKey = problemKey;
    }

    public String getProblemLandingPageURL()
    {
        return problemLandingPageURL;
    }

    public void setProblemLandingPageURL(String problemLandingPageURL)
    {
        this.problemLandingPageURL = problemLandingPageURL;
    }
}
