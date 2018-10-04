package io.yetanotherwhatever.ocpv2;

import io.yetanotherwhatever.ocpv2.aws.OutputResults;

import java.io.IOException;

/**
 * Created by achang on 9/3/2018.
 */
public interface IOcpV2DB {

        public void write(Invitation i) throws IOException;

        public void updateInvitation(String invitationId, String outputUploadDate, boolean success) throws IOException;

        public Invitation getInvitation(String invitationId) throws IOException;

        public OutputResults getOutputResults(String outputId) throws IOException;

        public void write(OutputResults or) throws IOException;
}