package com.yetanotherwhatever.ocpv2;

import java.io.InputStream;

/**
 * Created by achang on 9/17/2018.
 */
public interface IOutputStore {

    InputStream getTestOutput();

    InputStream getExpectedOutput();
}
