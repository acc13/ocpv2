package io.yetanotherwhatever.ocpv2;

import java.io.IOException;

/**
 * Created by achang on 9/7/2018.
 */
public interface ICodingProblemBuilder {

    CodingProblem buildCodingProblem() throws IOException;

    public int getExpirationInDays();
}
