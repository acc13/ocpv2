package com.yetanotherwhatever.ocpv2.lambdas;

import java.util.Date;

/**
 * Created by achang on 9/3/2018.
 */
public class Invitation {

    private String first;
    private String last;
    private String email;
    private String managerEmail;
    private Date date;

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public String getEmail() {
        return email;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public Date getDate() {
        return date;
    }

    public Invitation (String first, String last,
                       String email, String managerEmail)
    {
        this.first = first;
        this.last = last;
        this.email = email;
        this.managerEmail = managerEmail;
        date = new Date();
    }

    public String getValidationError()
    {
        return "";
    }
}
