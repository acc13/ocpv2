package io.yetanotherwhatever.ocpv2;



/*this class represents the state of the candidates progress through the coding problem.
The stages are:
A candidate is invited/registers (Invitation)
A candidate gets a problem page (CodingProblem)
A candidate tests their output (OutputTestHistory)

Finally, a candidate uploads their code and resume.

 */
public class CandidateWorkflow {

    Invitation invitation;

    CodingProblem codingProblem;

    OutputTestHistory outputTestHistory;

    public CandidateWorkflow(Invitation invitation, CodingProblem codingProblem)
    {
        this.invitation = invitation;
        this.codingProblem = codingProblem;
        this.outputTestHistory = new OutputTestHistory();
    }

    public CandidateWorkflow(Invitation invitation, CodingProblem codingProblem, OutputTestHistory history)
    {
        this.invitation = invitation;
        this.codingProblem = codingProblem;
        this.outputTestHistory = history;
    }

    public CandidateWorkflow setInvitation(Invitation invite)
    {
        this.invitation = invite;
        return this;
    }

    public Invitation getInvitation()
    {
        return invitation;
    }

    public CandidateWorkflow setCodingProblem(CodingProblem codingProblem) {
        this.codingProblem = codingProblem;
        return this;
    }

    public CodingProblem getCodingProblem()
    {
        return codingProblem;
    }

    public CandidateWorkflow setOutputTestHistory(OutputTestHistory outputTestHistory)
    {
        this.outputTestHistory = outputTestHistory;
        return this;
    }

    public OutputTestHistory getOutputTestHistory() {
        return outputTestHistory;
    }
}
