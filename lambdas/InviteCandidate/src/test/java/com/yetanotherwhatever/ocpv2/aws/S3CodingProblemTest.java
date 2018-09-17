package com.yetanotherwhatever.ocpv2.aws;

import org.junit.Test;

import static com.yetanotherwhatever.ocpv2.aws.S3CodingProblem.pickOne;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by achang on 9/9/2018.
 */
public class S3CodingProblemTest {

    @Test
    public void pickOne_works()
    {
        int[] counts = new int[5];

        int i = 100;
        while(0 < i--)
        {
            counts[pickOne(counts.length)]++;
        }

        for(i = 0; i < counts.length; i++)
        {
            assertThat(counts[i], is(not(0)));
        }
    }
}
