package io.yetanotherwhatever.ocpv2;

import java.io.IOException;

/**
 * Created by achang on 9/3/2018.
 */
public interface IOcpV2DB {

        public void write(CandidateWorkflow i) throws IOException;

        public void updateOutputTestHistory(String registrationId, String outputUploadDate, boolean success) throws IOException;

        public CandidateWorkflow getWorkflow(String registrationId) throws IOException;

        public OutputResults getOutputResults(String outputId) throws IOException;

        public void write(OutputResults or) throws IOException;
}
