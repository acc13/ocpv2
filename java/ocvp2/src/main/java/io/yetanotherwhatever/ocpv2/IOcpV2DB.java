package io.yetanotherwhatever.ocpv2;

import java.io.IOException;

/**
 * Created by achang on 9/3/2018.
 */
public interface IOcpV2DB {

        public void write(CandidateRegistration i) throws IOException;

        public void updateRegistration(String registrationId, String outputUploadDate, boolean success) throws IOException;

        public CandidateRegistration getRegistration(String registrationId) throws IOException;

        public OutputResults getOutputResults(String outputId) throws IOException;

        public void write(OutputResults or) throws IOException;
}
