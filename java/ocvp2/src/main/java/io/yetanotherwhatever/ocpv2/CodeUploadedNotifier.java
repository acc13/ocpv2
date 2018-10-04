package io.yetanotherwhatever.ocpv2;

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

    public void notifyManager(String invitationId, String downloadUrl) throws IOException
    {
        if (null == db || null == emailer || null == fileStore)
        {
            throw new IllegalStateException("CodeUploadedNotifier not initialized.");
        }
        if (null == invitationId || null == downloadUrl)
        {
            throw new IllegalArgumentException("invitationId and downloadUrl cannot be null.");
        }

        //look up invitation
        Invitation i = db.getInvitation(invitationId);

        //build upload url
        logger.debug("Download URL: " + downloadUrl);

        //email manager
        String subject = "Coding problem solution submitted";
        String body = buildEmailBody(i, downloadUrl);

        logger.debug("Emailing manager: " + i.getManagerEmail());
        logger.debug("Email subject: " + subject);
        logger.debug("Email body: " + body);

        emailer.sendEmail(i.getManagerEmail(), subject, body);
        logger.debug("Email successfully sent.");
    }

    private String buildEmailBody(Invitation i, String zipFileUrl)
    {
        String body =
                "Time: " + Utils.formatDateISO8601(new Date()) + "<br/>" +
                "\nCandidate: " + i.getCandidateFirstName() + " " + i.getCandidateLastName() + "<br/>" +
                "\nCandidate email: " + i.getCandidateEmail() + "<br/>" +
                "\nInvited on: " + i.getCreationDate() + "<br/>" +
                "\nProblem assigned: " + i.getProblemKey() + "<br/>" +
                "\nOutput uploaded attempts: " + i.getAttempts() + "<br/>" +
                "\nOutput passed: " + i.getSucceeded() + "<br/>" +
                "\nDownload submitted code <a href='" + zipFileUrl + "'>here</a>";

        return body;
    }

}

