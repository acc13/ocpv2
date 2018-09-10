package com.yetanotherwhatever.ocpv2.lambdas;

import java.io.IOException;

/**
 * Created by achang on 9/7/2018.
 */
public interface ICodingProblem {

    void setup() throws IOException;

    String getLandingPageURL();

    String getProblemKey();

    String getProblemGuid();
}
