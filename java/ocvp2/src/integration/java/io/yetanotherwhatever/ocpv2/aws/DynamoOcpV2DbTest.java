package io.yetanotherwhatever.ocpv2.aws;

import io.yetanotherwhatever.ocpv2.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by achang on 10/27/2018.
 */
public class DynamoOcpV2DbTest {

    @Test
    public void writeRead_savingWorkFlowEqualsLookup() throws IOException
    {
        DynamoOcpV2DB db = new DynamoOcpV2DB();

        Invitation i = new Invitation()
                .setCandidateEmail("candidate@email.com")
                .setManagerEmail("manager@company.com");
        CodingProblem cp = new CodingProblem()
                .setGuid("foo");
        OutputTestHistory o = new OutputTestHistory();

        CandidateWorkflow wf = new CandidateWorkflow(i, cp, o);

        db.delete(wf);
        db.write(wf);

        CandidateWorkflow readWf = db.getWorkflow("foo");

        assertThat(readWf, is(equalTo(wf)));

        db.delete(wf);
    }

    @Test
    public void writeRead_savingWorkflowInvitationEqualsLookup() throws IOException
    {
        DynamoOcpV2DB db = new DynamoOcpV2DB();

        Invitation i = new Invitation()
                .setCandidateEmail("candidate@email.com")
                .setManagerEmail("manager@company.com")
                .setCandidateFirstName("first")
                .setCandidateLastName("last")
                .setInvitationDate("invitationdate")
                .setResumeUrl("resumeurl")
                .setType(Invitation.Type.INTERN);

        CodingProblem cp = new CodingProblem()
                .setGuid("bar");
        OutputTestHistory o = new OutputTestHistory();

        CandidateWorkflow wf = new CandidateWorkflow(i, cp, o);

        db.write(wf);

        CandidateWorkflow readWf = db.getWorkflow("bar");

        Invitation readI = readWf.getInvitation();

        assertThat(readI.getCandidateFirstName(), is(equalTo(i.getCandidateFirstName())));
        assertThat(readI.getCandidateLastName(), is(equalTo(i.getCandidateLastName())));
        assertThat(readI.getCandidateEmail(), is(equalTo(i.getCandidateEmail())));
        assertThat(readI.getManagerEmail(), is(equalTo(i.getManagerEmail())));
        assertThat(readI.getInvitationDate(), is(equalTo(i.getInvitationDate())));
        assertThat(readI.getResumeUrl(), is(equalTo(i.getResumeUrl())));
        assertThat(readI.getType(), is(equalTo(i.getType())));

        db.delete(wf);
    }

    @Test
    public void writeRead_savingWorkflowCodingProblemEqualsLookup() throws IOException
    {
        DynamoOcpV2DB db = new DynamoOcpV2DB();

        Invitation i = new Invitation()
                .setCandidateEmail("candidate@email.com")
                .setManagerEmail("manager@company.com");
        CodingProblem cp = new CodingProblem()
                .setGuid("baz")
                .setExpirationDate("expirydate")
                .setLandingPageUrl("landingpage")
                .setName("problemname");
        OutputTestHistory o = new OutputTestHistory();

        CandidateWorkflow wf = new CandidateWorkflow(i, cp, o);

        db.delete(wf);
        db.write(wf);

        CandidateWorkflow readWf = db.getWorkflow("baz");

        CodingProblem readCP = readWf.getCodingProblem();

        assertThat(readCP.getName(), is(equalTo(cp.getName())));
        assertThat(readCP.getExpirationDate(), is(equalTo(cp.getExpirationDate())));
        assertThat(readCP.getGuid(), is(equalTo(cp.getGuid())));
        assertThat(readCP.getLandingPageUrl(), is(equalTo(cp.getLandingPageUrl())));

        db.delete(wf);
    }

    @Test
    public void writeRead_savingWorkflowOutputTestHistoryEqualsLookup() throws IOException
    {
        DynamoOcpV2DB db = new DynamoOcpV2DB();

        Invitation i = new Invitation()
                .setCandidateEmail("candidate@email.com")
                .setManagerEmail("manager@company.com");
        CodingProblem cp = new CodingProblem()
                .setGuid("yo");
        OutputTestHistory oth = new OutputTestHistory()
                .setAttempts(55)
                .setCodeSolutionUrl("thisurl")
                .setSucceeded("yes");

        CandidateWorkflow wf = new CandidateWorkflow(i, cp, oth);

        db.delete(wf);
        db.write(wf);

        CandidateWorkflow readWf = db.getWorkflow("yo");

        OutputTestHistory readOH = readWf.getOutputTestHistory();

        assertThat(readOH.getCodeSolutionUrl(), is(equalTo(oth.getCodeSolutionUrl())));
        assertThat(readOH.getAttempts(), is(equalTo(oth.getAttempts())));
        assertThat(readOH.getSucceeded(), is(equalTo(oth.getSucceeded())));

        db.delete(wf);
    }

    @Test
    public void getOutputResults_saveAndRetrieveWorksAsExpected() throws IOException
    {
        DynamoOcpV2DB db = new DynamoOcpV2DB();

        OutputResults or = new OutputResults()
                .setInvitationId("invitationId")
                .setResults("resultstring")
                .setUploadDate("uploaddate")
                .setUploadID("uploadId");

        db.delete(or);
        db.write(or);

        OutputResults readOR = db.getOutputResults("uploadId");

        assertThat(readOR.getInvitationId(), is(equalTo(or.getInvitationId())));
        assertThat(readOR.getResults(), is(equalTo(or.getResults())));
        assertThat(readOR.getUploadDate(), is(equalTo(or.getUploadDate())));
        assertThat(readOR.getUploadID(), is(equalTo(or.getUploadID())));

        db.delete(or);
    }

    private CandidateWorkflow createCandidateWorkflow(String candidateEmail, String managerEmail, String id)
    {
        Invitation i = new Invitation()
                .setCandidateEmail(candidateEmail)
                .setManagerEmail(managerEmail);
        CodingProblem cp = new CodingProblem()
                .setGuid(id);
        OutputTestHistory oth = new OutputTestHistory();

        return new CandidateWorkflow(i, cp, oth);
    }

    @Test
    public void listInterns_addedInternInvites_appearInQueriedList() throws IOException
    {
        DynamoOcpV2DB db = new DynamoOcpV2DB();

        CandidateWorkflow cwf1 = createCandidateWorkflow("candidate1", "manager1", "id1");
        CandidateWorkflow cwf2 = createCandidateWorkflow("candidate2", "manager2", "id2");
        CandidateWorkflow cwf3 = createCandidateWorkflow("candidate3", "manager3", "id3");

        cwf1.getInvitation().setType(Invitation.Type.INTERN);
        cwf2.getInvitation().setType(Invitation.Type.INTERN);
        cwf3.getInvitation().setType(Invitation.Type.FULL_TIME);

        db.delete(cwf1);
        db.delete(cwf2);
        db.delete(cwf3);

        List<CandidateWorkflow> internList = db.listAllInterns();

        assertThat(internList, not(hasItem(cwf1)));
        assertThat(internList, not(hasItem(cwf2)));
        assertThat(internList, not(hasItem(cwf3)));

        db.write(cwf1);
        db.write(cwf2);
        db.write(cwf3);

        internList = db.listAllInterns();

        assertThat(internList, hasItem(cwf1));
        assertThat(internList, hasItem(cwf2));
        assertThat(internList, not(hasItem(cwf3))); //full time invite

        db.delete(cwf1);
        db.delete(cwf2);
        db.delete(cwf3);
    }
}
