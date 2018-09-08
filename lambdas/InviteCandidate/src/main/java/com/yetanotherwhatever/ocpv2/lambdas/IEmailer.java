package com.yetanotherwhatever.ocpv2.lambdas;

interface IEmailer {

    void sendEmail(String destEmailAddress, String subject, String bodyText);
}
