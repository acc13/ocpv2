package com.yetanotherwhatever.ocpv2.lambdas;

import java.util.Date;

/**
 * Created by achang on 9/3/2018.
 */
public class Invitation {

    private String candidateFirstName;
    private String candidateLastName;
    private String candidateEmail;
    private String managerEmail;
    private Date date;

    public String getCandidateFirstName() {
        return candidateFirstName;
    }

    public Invitation setCandidateFirstName(String candidateFirstName) {
        this.candidateFirstName = candidateFirstName;

        return this;
    }

    public String getCandidateLastName() {
        return candidateLastName;
    }

    public Invitation setCandidateLastName(String candidateLastName) {
        this.candidateLastName = candidateLastName;

        return this;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public Invitation setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;

        return this;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public Invitation setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;

        return this;
    }

    public Date getDate() {
        return date;
    }

    public Invitation ()
    {
        date = new Date();
    }
}
