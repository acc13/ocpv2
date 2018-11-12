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
import io.yetanotherwhatever.ocpv2.Invitation;
import io.yetanotherwhatever.ocpv2.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public void rebuildProblemPages(String pagesToFix) {

        DynamoOcpV2DB db = new DynamoOcpV2DB();
        List<CandidateWorkflow> interns = db.listAllRegistrations();

        String[] ids = pagesToFix.split(",");

        System.out.println("ids length = " + ids.length);

        List<CandidateWorkflow> brokenPages = interns.stream()
                .filter(i -> Arrays.asList(ids).contains(i.getCodingProblem().getGuid()))
                .collect(Collectors.toList());

        System.out.println("pages to repair = " + brokenPages.size());

        brokenPages.stream()
                .forEach(i -> System.out.println(i.getInvitation().getCandidateEmail()));

        brokenPages.stream()
                .forEach(i -> System.out.println(i.getCodingProblem().getLandingPageUrl()));

        S3CodingProblemBuilder probBuiklder = new S3CodingProblemBuilder();
        brokenPages.stream()
                .forEach(i -> probBuiklder.copyProblem(i.getCodingProblem().getGuid(),
                        i.getCodingProblem().getName()));
    }


    static public void main(String[] args) throws ParseException
    {
        RepairInternSolutionUrls repair = new RepairInternSolutionUrls();

        //repair.findDupeRegistartions();
        //String raw = "0e057d3b-3ae5-412e-a3c5-7805a891d5ab,10bc6f7f-a90f-4d53-82ac-c9de8ee0a5e8,1252512a-4737-4e16-973a-8e2763fdf085,141c2f1b-2426-4681-8527-a6dd84bd1bf2,21e1c306-8949-4285-8ba0-971e0546ce5e,4c6b10dd-e135-4534-98df-bddca4c90e23,71fc1aed-01d0-4ba3-8a31-a840d5be25e9,7c4716c0-7aa3-41c8-9165-0f95cf0d97d0,80228ca7-6970-40b5-a4a9-307402672f2a,7669e964-a653-4678-80bd-c40ba12ec4d9,896fd12f-da71-4856-9fdd-8ebab312cdfe,a7d184ca-500f-4b46-8b01-95d31df10bd4,a39e869b-be81-485a-9c22-4df8d2ca1378,a833154b-08e1-48f1-99ed-4edf58745a25,ad6b3efd-4b86-4d2c-b887-d7734c3c2871,b3d413cf-06d1-488f-a86d-dbaa5e85bbd3,bfc37e82-eb13-407a-8808-d59dfdcc683e,beef136d-5f90-405b-af3a-1d346207f661,c3917d73-aa25-4fe6-86c1-d01c5498f8d9,d4497883-4a12-4089-97f8-325ac90b0613,f003e245-507f-42cd-9e03-4ed692cbb24c,e768d874-77de-4b7c-bc44-22d99a874c7a,f3ad97a0-e01f-4182-8d05-cd99e1fa879d.html";
        //repair.rebuildProblemPages(raw);
    }


}
