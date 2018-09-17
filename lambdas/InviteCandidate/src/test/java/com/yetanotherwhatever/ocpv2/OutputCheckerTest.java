package com.yetanotherwhatever.ocpv2;

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.yetanotherwhatever.ocpv2.OutputChecker.doStreamsMatch;
import static java.lang.String.join;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by achang on 9/17/2018.
 */
public class OutputCheckerTest {

    final static String NL= System.getProperty("line.separator");

    @Test
    public void doStreamsMatch_matchingInputs_success() throws IOException
    {
        String a = join(NL, "a", "b", "c");
        String b = join(NL, "a", "b", "c");
        InputStream isA = new ByteArrayInputStream( a.getBytes() );
        InputStream isB = new ByteArrayInputStream( b.getBytes() );

        assertTrue(doStreamsMatch(isA, isB));
    }
}
