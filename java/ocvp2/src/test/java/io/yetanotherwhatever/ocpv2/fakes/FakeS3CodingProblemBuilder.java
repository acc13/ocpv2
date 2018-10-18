package io.yetanotherwhatever.ocpv2.fakes;

import io.yetanotherwhatever.ocpv2.CodingProblem;
import io.yetanotherwhatever.ocpv2.ICodingProblemBuilder;

import java.io.IOException;

/**
 * Created by achang on 10/17/2018.
 */
public class FakeS3CodingProblemBuilder implements ICodingProblemBuilder{

    CodingProblem lastCpBuilt = null;

    @Override
    public CodingProblem buildCodingProblem() throws IOException {
        CodingProblem cp = new CodingProblem();
        cp.setGuid("fakeCodingProblemGuid");
        cp.setLandingPageUrl("https://foobar.com/tp/" + cp.getGuid() + ".html");
        cp.setName("testProblem");
        cp.setExpirationDate("");

        lastCpBuilt = cp;

        return cp;
    }

    @Override
    public int getExpirationInDays() {
        return 7;
    }

    public CodingProblem getLastCodingProblemBuilt()
    {
        return lastCpBuilt;
    }

}
