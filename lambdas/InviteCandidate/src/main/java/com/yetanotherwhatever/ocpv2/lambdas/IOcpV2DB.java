package com.yetanotherwhatever.ocpv2.lambdas;

import java.io.IOException;

/**
 * Created by achang on 9/3/2018.
 */
interface IOcpV2DB {

        public void write(Invitation i) throws IOException;
}
