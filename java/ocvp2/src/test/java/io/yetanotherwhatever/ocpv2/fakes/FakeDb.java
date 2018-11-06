package io.yetanotherwhatever.ocpv2.fakes;

import io.yetanotherwhatever.ocpv2.CandidateWorkflow;
import io.yetanotherwhatever.ocpv2.IOcpV2DB;
import io.yetanotherwhatever.ocpv2.Invitation;
import io.yetanotherwhatever.ocpv2.OutputResults;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeDb implements IOcpV2DB {

    Map<String, CandidateWorkflow> candidateWorkflows = new HashMap<>();
    Map<String, OutputResults> outputResults = new HashMap<>();
    

    @Override
    public void write(CandidateWorkflow cw) throws IOException {

        candidateWorkflows.put(cw.getCodingProblem().getGuid(), cw);
    }

    @Override
    public void updateOutputTestHistory(String problemPageId, String outputUploadDate, boolean success) throws IOException {
        //TODO
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

    @Override
    public List<CandidateWorkflow> listAllInterns() {
        List<CandidateWorkflow> interns = candidateWorkflows.values().stream()
                .filter(e -> e.getInvitation().getType() == Invitation.Type.INTERN)
                .collect(Collectors.toList());
        return interns;
    }
}
