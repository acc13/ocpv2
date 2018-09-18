package com.yetanotherwhatever.ocpv2.aws;

import java.util.Date;

/**
 * Created by achang on 9/17/2018.
 */
public class OutputResults {

    Date uploadDate;
    String results;
    String invitationId;
    String uploadID;

    public OutputResults()
    {

    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public String getUploadID() {
        return uploadID;
    }

    public void setUploadID(String uploadID) {
        this.uploadID = uploadID;
    }

}
