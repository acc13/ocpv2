package io.yetanotherwhatever.ocpv2;

import java.io.IOException;

public interface IEmailer {

    boolean sendEmail(String destEmailAddress, String subject, String bodyText) throws IOException;
}
