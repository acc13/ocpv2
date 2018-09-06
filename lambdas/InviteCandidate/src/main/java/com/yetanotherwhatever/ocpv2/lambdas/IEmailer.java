package com.yetanotherwhatever.ocpv2.lambdas;

public interface IEmailer {

    public void sendEmail(String email, String sub, String text);
}
