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

public class OCPDBUtils {

    // fix intern registration records that did not
    // properly update the outputTestHistory
    // (records created prior to fix made in commit 9802c334ec58264891845310be76a1868e06ead9
    // https://github.com/acc13/ocpv2/commit/9802c334ec58264891845310be76a1868e06ead9
    public void repairEmptySolutionUrls() {

        DynamoOcpV2DB db = new DynamoOcpV2DB();

        List<CandidateWorkflow> interns = db.listAllInterns();

        List<CandidateWorkflow> toRepair = interns.stream()
                .filter(i -> i.getOutputTestHistory().getCodeSolutionUrl() == null ||
                        i.getOutputTestHistory().getCodeSolutionUrl().length() == 0)
                .filter(i -> didInternRegisterBeforeFix(i))
                .collect(toList());

        List<S3ObjectSummary> solutions = listSolutionsPostedToS3();

        toRepair.stream().forEach(i -> fixInternCodeUploadUrl(solutions, i));

        toRepair.stream()
                .filter(i -> i.getOutputTestHistory().getCodeSolutionUrl() != null
                        && i.getOutputTestHistory().getCodeSolutionUrl().length() > 0)
                .forEach(i -> saveRecord(db, i));
    }

    public void fixBrokenInternCodeUploadUrls() {

        DynamoOcpV2DB db = new DynamoOcpV2DB();

        List<CandidateWorkflow> interns = db.listAllInterns();

        List<CandidateWorkflow> toRepair = interns.stream()
                .filter(i -> partialUrl(i))
                .collect(toList());

        List<S3ObjectSummary> solutions = listSolutionsPostedToS3();

        toRepair.stream().forEach(i -> fixInternCodeUploadUrl(solutions, i));

        toRepair.stream()
                .forEach(i -> saveRecord(db, i));
    }

    private boolean partialUrl(CandidateWorkflow intern)
    {
        return intern.getOutputTestHistory().getCodeSolutionUrl() != null &&
                intern.getOutputTestHistory().getCodeSolutionUrl().startsWith("uploads");
    }

    private void fixInternCodeUploadUrl(List<S3ObjectSummary> solutions, CandidateWorkflow intern)
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
            printInternDetails(intern);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void printInternDetails(CandidateWorkflow intern)
    {
        System.out.println(intern.getInvitation().getInvitationDate()
                + " " + intern.getInvitation().getCandidateEmail()
                + " " + intern.getCodingProblem().getGuid());
        System.out.println(intern.getOutputTestHistory().getCodeSolutionUrl());

        System.out.println();
        System.out.println();
    }

    // bug was fixed on 10/26/2018
    // github commit 9802c334ec58264891845310be76a1868e06ead9
    // https://github.com/acc13/ocpv2/commit/9802c334ec58264891845310be76a1868e06ead9
    private boolean didInternRegisterBeforeFix(CandidateWorkflow intern)
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

    private List<S3ObjectSummary> listSolutionsPostedToS3()
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

    public void printDupeInternRegistrations()     {

        DynamoOcpV2DB db = new DynamoOcpV2DB();

        List<CandidateWorkflow> interns = db.listAllInterns();
        System.out.println("bar" + interns.size());

        interns.stream()
                .forEach(i -> i.getCodingProblem().setGuid(i.getInvitation().getCandidateEmail()));

        List<CandidateWorkflow> cleanup = interns.stream()
                .filter(i -> Collections.frequency(interns, i) >1).collect(toList());

        System.out.println("foo" + cleanup.size());

        cleanup.stream()
                .forEach(i -> printInternDetails(i));
    }

    public void printInternCounts()
    {
        DynamoOcpV2DB db = new DynamoOcpV2DB();

        List<CandidateWorkflow> interns = db.listAllInterns();
        interns = interns.stream()
                .filter(i -> !i.getInvitation().getCandidateEmail().contains("hotmail.com") && !i.getInvitation().getCandidateEmail().contains("cornell.edu")).collect(toList());

        System.out.println("Interns registered: " + interns.stream().count());
        System.out.println("Interns submitted coding problem: " +
                interns.stream().filter(i -> i.getOutputTestHistory().getCodeSolutionUrl() != null   && i.getOutputTestHistory().getCodeSolutionUrl().length() > 0).count());
    }

    public void internExportToCsv()
    {
        DynamoOcpV2DB db = new DynamoOcpV2DB();

        List<CandidateWorkflow> interns = db.listAllInterns();

        System.out.println("First,Last,Email,Registration Date,Code Solution,Problem Name");

        interns.stream()
                .filter(i -> !i.getInvitation().getCandidateEmail().contains("hotmail.com") && !i.getInvitation().getCandidateEmail().contains("cornell.edu"))
                .forEach(i -> printInternCsv(i));
    }

    private void printInternCsv(CandidateWorkflow i) {

        String csv = String.join(",",
                i.getInvitation().getCandidateFirstName(),
                i.getInvitation().getCandidateLastName(),
                i.getInvitation().getCandidateEmail(),
                i.getInvitation().getInvitationDate().substring(0, 10),
                i.getOutputTestHistory().getCodeSolutionUrl(),
                i.getCodingProblem().getName());


        System.out.println(csv);
    }


    static public void main(String[] args) throws ParseException
    {
        OCPDBUtils repair = new OCPDBUtils();

        repair.internExportToCsv();
    }
}
