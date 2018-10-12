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

    public void setInvitation(Invitation invite)
    {
        this.invitation = invite;
    }

    public Invitation getInvitation()
    {
        return invitation;
    }

    public void setCodingProblem(CodingProblem codingProblem) {
        this.codingProblem = codingProblem;
    }

    public CodingProblem getCodingProblem()
    {
        return codingProblem;
    }

    public void setOutputTestHistory(OutputTestHistory outputTestHistory)
    {
        this.outputTestHistory = outputTestHistory;
    }

    public OutputTestHistory getOutputTestHistory() {
        return outputTestHistory;
    }
}
