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
        CandidateWorkflow cr = db.getWorkflow(invitationId);
        cr.getOutputTestHistory().setCodeSolutionUrl(downloadUrl);

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

    private String buildEmailBody(CandidateWorkflow cr, String zipFileUrl)
    {
        String body =
                "Time: " + Utils.formatDateISO8601(new Date()) + "<br/>" +
                "\n\nCandidate: " + cr.getInvitation().getCandidateFirstName() + " " + cr.getInvitation().getCandidateLastName() + "<br/>" +
                "\n\nCandidate email: " + cr.getInvitation().getCandidateEmail() + "<br/>" +
                "Candidate resume: " + (null != cr.getInvitation().getResumeUrl() && cr.getInvitation().getResumeUrl().length() > 0?
                                    "<a href='" + cr.getInvitation().getResumeUrl() + "'>" + cr.getInvitation().getResumeUrl() + "</a>" :
                                    "Not available.") + "<br/>" +
                "\n\nInvited on: " + cr.getInvitation().getInvitationDate() + "<br/>" +
                "\n\nProblem assigned: " + cr.getCodingProblem().getName() + "<br/>" +
                "\n\nOutput uploaded attempts: " + cr.getOutputTestHistory().getAttempts() + "<br/>" +
                "\n\nCorrect output uploaded: " + cr.getOutputTestHistory().getSucceeded() + "<br/>" +
                "\n\nDownload submitted code: <a href='" + zipFileUrl + "'>" + zipFileUrl + "</a>";

        return body;
    }

}

