package io.yetanotherwhatever.ocpv2;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by achang on 10/27/2018.
 */
public class EqualsTest {

    @Test
    public void candidateWorkflowEquals_sameData_true()
    {
        Invitation i = new Invitation();
        CodingProblem p = new CodingProblem()
                .setGuid("foo");
        OutputTestHistory o = new OutputTestHistory();

        CandidateWorkflow cw1 = new CandidateWorkflow(i, p, o);
        CandidateWorkflow cw2 = new CandidateWorkflow(i, p, o);

        assertThat(cw1.equals(cw2), is(true));
    }

    @Test
    public void candidateWorkflowEquals_differentData_false()
    {
        Invitation i = new Invitation();

        CodingProblem p1 = new CodingProblem().setGuid("foo");
        CodingProblem p2 = new CodingProblem().setGuid("bar");
        OutputTestHistory o = new OutputTestHistory();

        CandidateWorkflow cw1 = new CandidateWorkflow(i, p1, o);
        CandidateWorkflow cw2 = new CandidateWorkflow(i, p2, o);

        assertThat(cw1.equals(cw2), is(false));
    }

    @Test
    public void codingProblemEquals_sameData_true()
    {
        CodingProblem p1 = new CodingProblem()
                .setGuid("foo");
        CodingProblem p2 = new CodingProblem()
                .setGuid("foo");

        assertThat(p1.equals(p2), is(true));
    }

    @Test
    public void codingProblemEquals_differentData_false()
    {
        CodingProblem p1 = new CodingProblem()
                .setGuid("foo");
        CodingProblem p2 = new CodingProblem()
                .setGuid("bar");

        assertThat(p1.equals(p2), is(false));
    }

    @Test
    public void invitationEquals_sameData_true()
    {
        Invitation i1 = new Invitation()
                .setCandidateEmail("foo")
                .setManagerEmail("bar");
        Invitation i2 = new Invitation()
                .setCandidateEmail("foo")
                .setManagerEmail("bar");

        assertThat(i1.equals(i2), is(true));
    }

    @Test
    public void invitationProblemEquals_differentData_false()
    {
        Invitation i1 = new Invitation()
                .setCandidateEmail("foo")
                .setManagerEmail("bar");
        Invitation i2 = new Invitation()
                .setCandidateEmail("nacho")
                .setManagerEmail("libre");

        assertThat(i1.equals(i2), is(false));
    }
}
