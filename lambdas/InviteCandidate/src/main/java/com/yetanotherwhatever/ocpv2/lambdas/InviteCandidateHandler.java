package com.yetanotherwhatever.ocpv2.lambdas;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * Created by achang on 9/3/2018.
 */

public class InviteCandidateHandler implements RequestHandler<Invitation, String>{

    @Override
    public String handleRequest(Invitation invitation, Context context) {

        String err = Inviter.isValid(invitation);
        if (null == err)
        {
            return new Inviter(new DynamoDB()).invite(invitation);
        }
        else
        {
            return err;
        }
    }
}