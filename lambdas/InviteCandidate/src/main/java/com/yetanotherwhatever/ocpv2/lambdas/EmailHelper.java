package com.yetanotherwhatever.ocpv2.lambdas;


import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;


/**
 * Created by achang on 9/3/2018.
 */
public class EmailHelper implements IEmailer {

    private static AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient();
    private static final String SENDER = "noreply@yetanotherwhatever.io>";

    public void sendEmail(String email, String sub, String text)
    {
        if (client == null) {
            client = new AmazonSimpleEmailServiceClient();
        }

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
    }
}
