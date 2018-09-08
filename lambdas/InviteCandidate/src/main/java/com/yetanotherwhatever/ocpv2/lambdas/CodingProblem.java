package com.yetanotherwhatever.ocpv2.lambdas;

import java.io.IOException;

/**
 * Created by achang on 9/3/2018.
 */
class CodingProblem {

    private String problemKey;
    private String problemGuid;
    private String landingPageURL;

    protected String getLandingPageURL() {

        return landingPageURL;
    }

    protected String getProblemKey() {
        return problemKey;
    }

    protected String getProblemGuid() {
        return problemGuid;
    }

    protected CodingProblem()
    {

    }

    protected void setup() throws IOException
    {

    }

}
