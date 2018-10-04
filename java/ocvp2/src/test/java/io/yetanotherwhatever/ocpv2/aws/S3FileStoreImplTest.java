package io.yetanotherwhatever.ocpv2.aws;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by achang on 9/18/2018.
 */
public class S3FileStoreImplTest {

    @Test
    public void buildDownloadUrl_validFileName_succeeds() throws IOException
    {
        S3FileStoreImpl s3FS = new S3FileStoreImpl();
        String url = s3FS.buildDownloadUrl("bucket", "key");
        assertThat(url,
                is(equalTo("https://s3.amazonaws.com/bucket/key")));
    }
}
