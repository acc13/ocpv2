package io.yetanotherwhatever.ocpv2.fakes;

import io.yetanotherwhatever.ocpv2.CandidateWorkflow;
import io.yetanotherwhatever.ocpv2.IOcpV2DB;
import io.yetanotherwhatever.ocpv2.OutputResults;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FakeDb implements IOcpV2DB {

    Map<String, CandidateWorkflow> candidateWorkflows = new HashMap<>();
    Map<String, OutputResults> outputResults = new HashMap<>();
    

    @Override
    public void write(CandidateWorkflow cw) throws IOException {

        candidateWorkflows.put(cw.getCodingProblem().getGuid(), cw);
    }

    @Override
    public void updateOutputTestHistory(String problemPageId, String outputUploadDate, boolean success) throws IOException {
        //do nothing
    }

    @Override
    public CandidateWorkflow getWorkflow(String problemPageId) throws IOException {
        return candidateWorkflows.get(problemPageId);
    }

    @Override
    public OutputResults getOutputResults(String outputId) throws IOException {
        return outputResults.get(outputId);
    }

    @Override
    public void write(OutputResults or) throws IOException {
        outputResults.put(or.getUploadID(), or);
    }
}
