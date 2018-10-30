package io.yetanotherwhatever.ocpv2;

import io.yetanotherwhatever.ocpv2.Utils;

import java.util.Date;

/**
 * Created by achang on 9/17/2018.
 */
public class OutputResults {

    String uploadDate;
    String results;
    String invitationId;
    String uploadID;

    public OutputResults()
    {

    }

    public String getUploadDate()
    {
        return uploadDate;
    }

    public OutputResults setUploadDate(Date uploadDate)
    {
        this.uploadDate = Utils.formatDateISO8601(uploadDate);
        return this;
    }

    public OutputResults setUploadDate(String uploadDate)
    {
        this.uploadDate = uploadDate;
        return this;
    }

    public String getResults()
    {
        return results;
    }

    public OutputResults setResults(String results)
    {
        this.results = results;
        return this;
    }

    public String getInvitationId()
    {
        return invitationId;
    }

    public OutputResults setInvitationId(String invitationId)
    {
        this.invitationId = invitationId;
        return this;
    }

    public String getUploadID()
    {
        return uploadID;
    }

    public OutputResults setUploadID(String uploadID)
    {
        this.uploadID = uploadID;
        return this;
    }

}
