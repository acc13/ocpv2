package com.yetanotherwhatever.ocpv2.lambdas;

import java.io.IOException;

interface IEmailer {

    void sendEmail(String destEmailAddress, String subject, String bodyText) throws IOException;
}
