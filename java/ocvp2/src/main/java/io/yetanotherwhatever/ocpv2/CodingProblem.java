package io.yetanotherwhatever.ocpv2;

public class CodingProblem {

    String guid;
    String name;
    String landingPageUrl;
    String succeeded;
    int attempts;


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLandingPageUrl() {
        return landingPageUrl;
    }

    public void setLandingPageUrl(String landingPageUrl) {
        this.landingPageUrl = landingPageUrl;
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
}
