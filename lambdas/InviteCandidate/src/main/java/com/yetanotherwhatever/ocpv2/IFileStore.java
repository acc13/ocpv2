package com.yetanotherwhatever.ocpv2;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by achang on 9/17/2018.
 */
public interface IFileStore {

    public InputStream readFile(String key) throws IOException;
}
