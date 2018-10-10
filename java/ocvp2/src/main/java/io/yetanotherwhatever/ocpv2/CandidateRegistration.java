package io.yetanotherwhatever.ocpv2;

public class CandidateRegistration {

    Invitation invitation;

    CodingProblem codingProblem;

    public void setInvitation(Invitation invite) {
        this.invitation = invite;
    }

    public void setProblemPage(CodingProblem codingProblem) {
        this.codingProblem = codingProblem;
    }

    public Invitation getInvitation()
    {
        return invitation;
    }

    public CodingProblem getCodingProblem()
    {
        return codingProblem;
    }
}
