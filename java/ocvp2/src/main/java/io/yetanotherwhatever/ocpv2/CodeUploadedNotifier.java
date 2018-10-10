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
        CandidateRegistration cr = db.getRegistration(invitationId);

        //build upload url
        logger.debug("Download URL: " + downloadUrl);

        //email manager
        String subject = "Coding problem solution submitted";
        String body = buildEmailBody(cr, downloadUrl);

        logger.debug("Emailing manager: " + cr.getInvitation().getManagerEmail());
        logger.debug("Email subject: " + subject);
        logger.debug("Email body: " + body);

        emailer.sendEmail(cr.getInvitation().getManagerEmail(), subject, body);
        logger.debug("Email successfully sent.");
    }

    private String buildEmailBody(CandidateRegistration cr, String zipFileUrl)
    {
        String body =
                "Time: " + Utils.formatDateISO8601(new Date()) + "<br/>" +
                "\nCandidate: " + cr.getInvitation().getCandidateFirstName() + " " + cr.getInvitation().getCandidateLastName() + "<br/>" +
                "\nCandidate email: " + cr.getInvitation().getCandidateEmail() + "<br/>" +
                "\nInvited on: " + cr.getInvitation().getInvitationDate() + "<br/>" +
                "\nProblem assigned: " + cr.getCodingProblem().getName() + "<br/>" +
                "\nOutput uploaded attempts: " + cr.getCodingProblem().getAttempts() + "<br/>" +
                "\nOutput passed: " + cr.getCodingProblem().getSucceeded() + "<br/>" +
                "\nDownload submitted code <a href='" + zipFileUrl + "'>here</a>";

        return body;
    }

}

