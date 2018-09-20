package com.yetanotherwhatever.ocpv2;

import com.yetanotherwhatever.ocpv2.aws.CodeUploadedHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;

/**
 * Created by achang on 9/18/2018.
 */
public class CodeUploadedNotifier {

    static final Logger logger = LogManager.getLogger(CodeUploadedNotifier.class);

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
        if (null == invitationId || null == zipFileName)
        {
            throw new IllegalArgumentException("invitationId and zipFileName cannot be null.");
        }

        //look up invitation
        Invitation i = db.read(invitationId);

        //build upload url
        String zipfileUrl = fileStore.buildDownloadUrl(zipFileName);
        logger.debug("Download URL: " + zipFileName);

        //email manager
        String subject = "Coding problem solution submitted";
        String body = buildEmailBody(i, zipfileUrl);

        logger.debug("Emailing manager: " + i.getManagerEmail());
        logger.debug("Email subject: " + subject);
        logger.debug("Email body: " + body);

        emailer.sendEmail(i.getManagerEmail(), subject, body);
        logger.debug("Email successfully sent.");
    }

    private String buildEmailBody(Invitation i, String zipFileUrl)
    {
        String body =
                "Time: " + Utils.formatDateISO8601(new Date()) +
                "\nCandidate: " + i.getCandidateFirstName() + " " + i.getCandidateLastName() +
                "\nCandidate email: " + i.getCandidateEmail() +
                "\nInvited on: " + i.getCreationDate() +
                "\nProblem assigned: " + i.getProblemKey() +
                "\nOutput uploaded attempts: " + i.getAttempts() +
                "\nOutput passed: " + i.getSucceeded() +
                "\nDownload submitted code here:" + zipFileUrl;

        return body;
    }

}

