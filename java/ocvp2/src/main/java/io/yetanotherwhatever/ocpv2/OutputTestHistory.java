package io.yetanotherwhatever.ocpv2;

public class OutputTestHistory {

    String succeeded;
    int attempts;
    String codeSolutionUrl;

    public OutputTestHistory()
    {
        succeeded = "Never";
        attempts = 0;
    }

    public String getSucceeded()
    {
        return succeeded;
    }

    public void setSucceeded(String succeeded)
    {
        this.succeeded = succeeded;
    }

    public int getAttempts()
    {
        return attempts;
    }

    public void setAttempts(int attempts)
    {
        this.attempts = attempts;
    }

    public String getCodeSolutionUrl() {
        return codeSolutionUrl;
    }

    public void setCodeSolutionUrl(String codeSolutionUrl) {
        this.codeSolutionUrl = codeSolutionUrl;
    }
}
