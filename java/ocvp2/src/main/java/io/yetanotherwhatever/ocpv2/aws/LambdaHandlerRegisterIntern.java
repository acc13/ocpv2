package io.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import io.yetanotherwhatever.ocpv2.Invitation;
import io.yetanotherwhatever.ocpv2.Inviter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Map;

public class LambdaHandlerRegisterIntern implements RequestHandler<S3Event,S3Event> {

    static final Logger logger = LogManager.getLogger(LambdaHandlerRegisterIntern.class);

    AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();

    public S3Event handleRequest(S3Event s3Event, Context context) {

        // For each record.
        for (S3EventNotification.S3EventNotificationRecord record : s3Event.getRecords()) {

            try {
                Invitation invitation = parseRecordMetadata(record);
                String downloadUrl = new S3FileStoreImpl().buildDownloadUrl(record);
                invitation.setResumeUrl(downloadUrl);

                new Inviter()
                        .setDB(new DynamoOcpV2DB())
                        .setCodingProblemBuilder(new S3CodingProblemBuilder())
                        .setEmailer(new SESEmailHelper())
                        .sendInvitation(invitation);
            }
            catch(IOException | IllegalArgumentException | ParseException e)
            {
                e.printStackTrace();
            }

        }

        return s3Event;

    }

    private Invitation parseRecordMetadata(S3EventNotification.S3EventNotificationRecord record) throws ParseException
    {
        String bucket = record.getS3().getBucket().getName();
        String key = record.getS3().getObject().getKey();

        S3Object fullObject = s3Client.getObject(new GetObjectRequest(bucket, key));

        Map<String, String> userMetadata = fullObject.getObjectMetadata().getUserMetadata();
        for (String mdKey : userMetadata.keySet())
        {
            logger.debug("Metadata " + mdKey + " : " + userMetadata.get(mdKey));
        }

        String inviteJson = userMetadata.get("data");

        return parseInvitationJson(inviteJson);
    }

    private Invitation parseInvitationJson(String inviteJson) throws ParseException
    {
        JSONParser parser = new JSONParser();

        JSONObject event = (JSONObject)parser.parse(inviteJson);

        Invitation i = new Invitation(Invitation.Type.INTERN);

        i.setCandidateFirstName((String) event.get("candidateFirst"));
        i.setCandidateLastName((String) event.get("candidateLast"));
        i.setCandidateEmail((String) event.get("candidateEmail"));
        i.setManagerEmail((String) event.get("managerEmail"));

        return i;
    }
}
