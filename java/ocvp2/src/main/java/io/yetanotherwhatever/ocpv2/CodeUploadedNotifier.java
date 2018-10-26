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

    public CodeUploadedNotifier setDb(IOcpV2DB db) {
        this.db = db;
        return this;
    }

    public CodeUploadedNotifier setEmailer(IEmailer emailer) {
        this.emailer = emailer;
        return this;
    }

    public CodeUploadedNotifier setFileStore(IFileStore fs) {
        this.fileStore = fs;
        return this;
    }

    public boolean notifyManager(String invitationId, String downloadUrl) throws IOException
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
        CandidateWorkflow cw = db.getWorkflow(invitationId);
        cw.getOutputTestHistory().setCodeSolutionUrl(downloadUrl);

        //build upload url
        logger.debug("Download URL: " + downloadUrl);

        //email manager
        String subject = buildEmailSubject(cw);
        String body = buildEmailBody(cw, downloadUrl);

        logger.debug("Emailing manager: " + cw.getInvitation().getManagerEmail());
        logger.debug("Email subject: " + subject);
        logger.debug("Email body: " + body);

        emailer.sendEmail(cw.getInvitation().getManagerEmail(), subject, body);
        logger.debug("Email successfully sent.");

        return true;
    }

    protected String buildEmailSubject(CandidateWorkflow cw)
    {
        return "Coding problem solution submitted: "
                + cw.getInvitation().getCandidateFirstName() + " "
                + cw.getInvitation().getCandidateLastName() + ", "
                + cw.getInvitation().getCandidateEmail();
    }

    protected String buildEmailBody(CandidateWorkflow cr, String zipFileUrl)
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

