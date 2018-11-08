package io.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import io.yetanotherwhatever.ocpv2.CandidateWorkflow;
import io.yetanotherwhatever.ocpv2.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static java.util.Calendar.MONTH;
import static java.util.stream.Collectors.toList;

public class RepairInternSolutionUrls {

    public void repairEmptySolutionUrls() {

        DynamoOcpV2DB db = new DynamoOcpV2DB();

        List<CandidateWorkflow> interns = db.listAllInterns();

        List<CandidateWorkflow> toRepair = interns.stream()
                .filter(i -> i.getOutputTestHistory().getCodeSolutionUrl() == null ||
                i.getOutputTestHistory().getCodeSolutionUrl().length() == 0)
                .filter(i -> registeredBeforeFix(i))
                .collect(toList());

        List<S3ObjectSummary> solutions = loadSolutions();

        toRepair.stream().forEach(i -> updateWorkflow(solutions, i));

        toRepair.stream()
                .filter(i -> i.getOutputTestHistory().getCodeSolutionUrl() != null
                    && i.getOutputTestHistory().getCodeSolutionUrl().length() > 0)
                .forEach(i -> saveRecord(db, i));
    }

    public void repairCurtailedUrls() {

        DynamoOcpV2DB db = new DynamoOcpV2DB();

        List<CandidateWorkflow> interns = db.listAllInterns();

        List<CandidateWorkflow> toRepair = interns.stream()
                .filter(i -> partialUrl(i))
                .collect(toList());

        List<S3ObjectSummary> solutions = loadSolutions();

        toRepair.stream().forEach(i -> updateWorkflow(solutions, i));

        toRepair.stream()
                .forEach(i -> saveRecord(db, i));
    }

    private boolean partialUrl(CandidateWorkflow intern)
    {
        return intern.getOutputTestHistory().getCodeSolutionUrl() != null &&
                intern.getOutputTestHistory().getCodeSolutionUrl().startsWith("uploads");
    }

    private void updateWorkflow(List<S3ObjectSummary> solutions, CandidateWorkflow intern)
    {
        S3ObjectSummary solution = solutions.stream().filter(o -> o.getKey().contains(intern.getCodingProblem().getGuid()))
                .reduce((a, b) -> (a.getLastModified().compareTo(b.getLastModified()) > 0 ? a : b))
                .orElse(null);

        if (solution != null)
        {
            //https://s3.amazonaws.com/ocp-upload-yetanotherwhatever/uploads/code/0715c60f-5570-477b-8211-f6737407cc80/494D2198-526C-42EA-96C1-4375AD9E8EA4.zip
            String prefix = "https://s3.amazonaws.com/ocp-upload-yetanotherwhatever/";
            intern.getOutputTestHistory().setCodeSolutionUrl(prefix + solution.getKey());
        }
    }

    private void saveRecord(DynamoOcpV2DB db, CandidateWorkflow intern)
    {
        try {
            db.write(intern);
            printIntern(intern);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void printIntern(CandidateWorkflow intern)
    {
        System.out.println(intern.getInvitation().getInvitationDate()
                + " " + intern.getInvitation().getCandidateEmail()
                + " " + intern.getCodingProblem().getGuid());
        System.out.println(intern.getOutputTestHistory().getCodeSolutionUrl());

        System.out.println();
        System.out.println();
    }

    private boolean registeredBeforeFix(CandidateWorkflow intern)
    {
        boolean beforeFix = false;
        try {
            beforeFix = Utils.unformatDateISO8601(intern.getInvitation().getInvitationDate()).get(MONTH) == Calendar.OCTOBER
                &&  Utils.unformatDateISO8601(intern.getInvitation().getInvitationDate()).get(Calendar.DAY_OF_MONTH) <= 26;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return beforeFix;
    }

    private List<S3ObjectSummary> loadSolutions()
    {
        String bucketName = "ocp-upload-yetanotherwhatever";

        StringBuilder results = new StringBuilder();
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .build();

            ListObjectsV2Request req = new ListObjectsV2Request()
                    .withBucketName(bucketName)
                    .withPrefix("uploads/code/");

            ListObjectsV2Result result = s3Client.listObjectsV2(req);

            return result.getObjectSummaries();
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }

        return null;
    }

    public void findDupeRegistartions()     {

        DynamoOcpV2DB db = new DynamoOcpV2DB();

        List<CandidateWorkflow> interns = db.listAllInterns();
        System.out.println("bar" + interns.size());

        interns.stream()
                .forEach(i -> i.getCodingProblem().setGuid(i.getInvitation().getCandidateEmail()));

        List<CandidateWorkflow> cleanup = interns.stream()
                .filter(i -> Collections.frequency(interns, i) >1).collect(toList());

        System.out.println("foo" + cleanup.size());

        cleanup.stream()
                .forEach(i -> printIntern(i));
    }


    static public void main(String[] args) throws ParseException
    {
        RepairInternSolutionUrls repair = new RepairInternSolutionUrls();

        //repair.findDupeRegistartions();
        repair.repairCurtailedUrls();
    }
}
