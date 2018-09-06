package com.yetanotherwhatever.ocpv2.lambdas;


import com.amazonaws.services.kinesis.model.InvalidArgumentException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;

/**
 * Created by achang on 9/3/2018.
 */

public class InviteCandidateHandler implements RequestHandler<Invitation, String>{

    @Override
    public String handleRequest(Invitation invitation, Context context) {

        try {
            new Inviter().setDB(new DynamoDB()).sendInvitation(invitation);

            return "SUCCESS";
        }
        catch(IOException | InvalidArgumentException e)
        {
            return "ERROR: " + e.getMessage();
        }


    }
}