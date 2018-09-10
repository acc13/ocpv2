package com.yetanotherwhatever.ocpv2.lambdas;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;

import java.io.IOException;


/**
 * Created by achang on 9/3/2018.
 */
class SESEmailHelper implements IEmailer {

    private static final AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient();
    private static final String SENDER = "noreply@yetanotherwhatever.io>";

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
        } catch (AmazonServiceException e)
        {
            throw new IOException(e);
        }
    }
}
