package io.yetanotherwhatever;


import org.junit.Test;

import java.util.Calendar;

import static io.yetanotherwhatever.S3HTTPPostFormSigner.*;
import static java.util.Calendar.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class S3HTTPPostFormSignerTest {

    @Test
    public void oneYearFrom_plusOne_1stbday()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(1977, OCTOBER, 5);
        assertThat(yearsFromToday(cal, 1), equalTo("1978-10-05"));
    }

    @Test
    public void oneYearFrom_plus5_5thbday()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(1977, OCTOBER, 5);
        assertThat(yearsFromToday(cal, 5), equalTo("1982-10-05"));
    }

    @Test
    public void b64Encode_happyPath()
    {
        assertThat(b64Encode(""), equalTo(""));
        assertThat(b64Encode("test"), equalTo("dGVzdA=="));

        //all whitespace removed
        assertThat(b64Encode("Ohmonamour,moncoeurestlourdJecomptelesheuresjecomptelesjours"),
                equalTo("T2htb25hbW91cixtb25jb2V1cmVzdGxvdXJkSmVjb21wdGVsZXNoZXVyZXNqZWNvbXB0ZWxlc2pvdXJz"));
    }
}
