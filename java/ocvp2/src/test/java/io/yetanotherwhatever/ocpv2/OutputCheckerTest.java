package io.yetanotherwhatever.ocpv2;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.join;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by achang on 9/17/2018.
 */
public class OutputCheckerTest {

    final static String NL= System.getProperty("line.separator");

    boolean doStreamsMatch(InputStream a, InputStream b) throws IOException
    {
        OutputChecker oc = new OutputChecker();

        return oc.doStreamsMatch(a, b);
    }

    @Test
    public void doStreamsMatch_matchingInputs_success() throws IOException
    {
        String a = join(NL, "a", "b", "c");
        String b = join(NL, "a", "b", "c");
        InputStream isA = new ByteArrayInputStream( a.getBytes() );
        InputStream isB = new ByteArrayInputStream( b.getBytes() );

        assertTrue(doStreamsMatch(isA, isB));
    }

    @Test
    public void doStreamsMatch_differentInputs_fail() throws IOException
    {
        String a = join(NL, "a", "b", "c");
        String b = join(NL, "x", "y", "z");
        InputStream isA = new ByteArrayInputStream( a.getBytes() );
        InputStream isB = new ByteArrayInputStream( b.getBytes() );

        assertFalse(doStreamsMatch(isA, isB));
    }

    @Test
    public void doStreamsMatch_surroundingWhitespace_ignored() throws IOException
    {
        String a = join(NL, "a ", "b", "    c      ");
        String b = join(NL, "a", " b ", "c");
        InputStream isA = new ByteArrayInputStream( a.getBytes() );
        InputStream isB = new ByteArrayInputStream( b.getBytes() );

        assertTrue(doStreamsMatch(isA, isB));
    }

    @Test
    public void doStreamsMatch_outputTooLong_fail() throws IOException
    {
        String a = join(NL, "a", "b", "c", "d");
        String b = join(NL, "a", "b", "c");
        InputStream isA = new ByteArrayInputStream( a.getBytes() );
        InputStream isB = new ByteArrayInputStream( b.getBytes() );

        assertFalse(doStreamsMatch(isA, isB));
    }

    @Test
    public void doStreamsMatch_outputTooShort_fail() throws IOException
    {
        String a = join(NL, "a", "b", "c");
        String b = join(NL, "a", "b", "c", "d");
        InputStream isA = new ByteArrayInputStream( a.getBytes() );
        InputStream isB = new ByteArrayInputStream( b.getBytes() );

        assertFalse(doStreamsMatch(isA, isB));
    }
}
