package com.yetanotherwhatever.ocpv2.aws;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.yetanotherwhatever.ocpv2.IEmailer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


/**
 * Created by achang on 9/3/2018.
 */
class SESEmailHelper implements IEmailer {

    static final Logger logger = LogManager.getLogger(SESEmailHelper.class);

    private static final AmazonSimpleEmailService client =
            AmazonSimpleEmailServiceClientBuilder.defaultClient();

    private static final String SENDER = "noreply@yetanotherwhatever.io";

    public void sendEmail(String email, String sub, String text) throws IOException
    {
        try {
            Destination destination = new Destination().withToAddresses(new String[]{email});
            Content subject = new Content().withData(sub);
            Content textContent = new Content().withData(text);
            Body body = new Body().withText(textContent);
            Message message = new Message()
                    .withSubject(subject)
                    .withBody(body);
            SendEmailRequest request = new SendEmailRequest()
                    .withSource(SENDER)
                    .withDestination(destination)
                    .withMessage(message);

            client.sendEmail(request);

            logger.info("Email sent to: " + email);
            logger.info("Email subject: " + sub);
            logger.info("Email body: " + text);

        } catch (AmazonServiceException e)
        {
            throw new IOException(e);
        }
    }
}
