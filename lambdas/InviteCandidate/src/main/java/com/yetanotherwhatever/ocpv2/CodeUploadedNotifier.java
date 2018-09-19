package com.yetanotherwhatever.ocpv2;

import java.io.IOException;

/**
 * Created by achang on 9/18/2018.
 */
public class CodeUploadedNotifier {

    IOcpV2DB db;
    IEmailer emailer;
    IFileStore fileStore;

    public CodeUploadedNotifier()
    {
    }

    public void setDb(IOcpV2DB db) {
        this.db = db;
    }

    public void setEmailer(IEmailer emailer) {
        this.emailer = emailer;
    }

    public void setFileStore(IFileStore fs) {
        this.fileStore = fs;
    }

    public void notifyManager(String invitationId, String zipFileName) throws IOException
    {
        if (null == db || null == emailer || null == fileStore)
        {
            throw new IllegalStateException("CodeUploadedNotifier not initialized.");
        }

        //look up invitation
        Invitation i = db.read(invitationId);

        //build upload url
        String zipfileUrl = fileStore.buildDownloadUrl(zipFileName);

        //email manager

    }

    private String buildEmailBody()
    {
        return null;
    }

}
