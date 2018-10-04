package io.yetanotherwhatever.ocpv2;

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
