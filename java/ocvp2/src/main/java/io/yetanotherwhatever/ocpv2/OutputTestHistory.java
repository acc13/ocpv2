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

    public OutputTestHistory setSucceeded(String succeeded)
    {
        this.succeeded = succeeded;
        return this;
    }

    public int getAttempts()
    {
        return attempts;
    }

    public OutputTestHistory setAttempts(int attempts)
    {
        this.attempts = attempts;
        return this;
    }

    public String getCodeSolutionUrl() {
        return codeSolutionUrl;
    }

    public OutputTestHistory setCodeSolutionUrl(String codeSolutionUrl) {
        this.codeSolutionUrl = codeSolutionUrl;
        return this;
    }
}
