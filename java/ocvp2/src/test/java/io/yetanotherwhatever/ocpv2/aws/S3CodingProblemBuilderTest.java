package io.yetanotherwhatever.ocpv2.aws;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by achang on 9/9/2018.
 */
public class S3CodingProblemBuilderTest {

    @Test
    public void pickOne_works()
    {
        int[] counts = new int[5];

        int i = 100;
        while(0 < i--)
        {
            counts[S3CodingProblemBuilderBuilder.pickOne(counts.length)]++;
        }

        for(i = 0; i < counts.length; i++)
        {
            assertThat(counts[i], is(not(0)));
        }
    }
}
