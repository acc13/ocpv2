package io.yetanotherwhatever.ocpv2;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof CandidateWorkflow)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        CandidateWorkflow c = (CandidateWorkflow) o;

        // Compare the data members and return accordingly
        return new EqualsBuilder()
                    .append(codingProblem, c.getCodingProblem())
                    .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31)
                        .append(codingProblem)
                        .toHashCode();
    }
}
