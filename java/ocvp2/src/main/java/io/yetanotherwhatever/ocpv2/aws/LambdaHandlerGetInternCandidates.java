package io.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import io.yetanotherwhatever.ocpv2.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


import static io.yetanotherwhatever.ocpv2.OutputTestResultGetter.HTTP_SUCCESS;

public class LambdaHandlerGetInternCandidates implements RequestStreamHandler {

    static final Logger logger = LogManager.getLogger(LambdaHandlerGetTestOutputResults.class);

    IOcpV2DB db = null;

    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException
    {

        if (db == null)
        {
            this.db = new DynamoOcpV2DB();
        }

        List<CandidateWorkflow> interns = db.listAllInterns();

        JSONObject responseJson = buildResponseJson(HTTP_SUCCESS, interns, null);


        logger.debug(responseJson.toJSONString());

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toJSONString());
        writer.close();
    }

    protected void setDb(IOcpV2DB db)
    {
        this.db = db;
    }

    /*
    Build response
    format described here: https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html
    JSON:
        {
            "isBase64Encoded": true|false,
            "statusCode": httpStatusCode,
            "headers": { "headerName": "headerValue", ... },
            "body": "..."
        }
    */
    private JSONObject buildResponseJson(String statusCode, List<CandidateWorkflow> interns, String exception)
    {
        JSONObject responseJson = new JSONObject();

        JSONObject headerJson = new JSONObject();
        headerJson.put("Access-Control-Allow-Origin", "*");
        responseJson.put("isBase64Encoded", false);
        responseJson.put("headers", headerJson);

        if (null != statusCode) {
            responseJson.put("statusCode", statusCode);
        }

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("result", buildInternListJason(interns));

        responseJson.put("body", bodyJson.toString());

        if (null != exception) {
            responseJson.put("exception", exception);
        }

        return responseJson;
    }

    private JSONArray buildInternListJason(List<CandidateWorkflow> interns)
    {
        JSONArray internList = new JSONArray();

        interns.stream().forEach(e -> internList.add(buildWorkflowJson(e)));

        return internList;
    }

    private JSONObject buildWorkflowJson(CandidateWorkflow cwf)
    {
        JSONObject workflowJson = new JSONObject();

        workflowJson.put("invitation", buildInvitationJson(cwf.getInvitation()));
        workflowJson.put("codingProblem", buildCodingProblemJson(cwf.getCodingProblem()));
        workflowJson.put("outputTestHistory", buildOutputTestHistoryJson(cwf.getOutputTestHistory()));

        return workflowJson;
    }

    private JSONObject buildInvitationJson(Invitation i)
    {
        JSONObject invitationJson = new JSONObject();
        invitationJson.put("registrationDate", i.getInvitationDate());
        invitationJson.put("candidateFirst", i.getCandidateFirstName());
        invitationJson.put("candidateLast", i.getCandidateLastName());
        invitationJson.put("candidateEmail", i.getCandidateEmail());
        invitationJson.put("managerEmail", i.getManagerEmail());
        invitationJson.put("resumeURL", i.getResumeUrl());

        return invitationJson;
    }

    private JSONObject buildCodingProblemJson(CodingProblem cp)
    {
        JSONObject codingProblemJson = new JSONObject();
        codingProblemJson.put("guid", cp.getGuid());
        codingProblemJson.put("landingPage", cp.getLandingPageUrl());
        codingProblemJson.put("name", cp.getName());

        return codingProblemJson;
    }

    private JSONObject buildOutputTestHistoryJson(OutputTestHistory oth)
    {
        JSONObject othJson = new JSONObject();
        othJson.put("attempts", oth.getAttempts());
        othJson.put("succeeded", oth.getSucceeded());
        othJson.put("solutionURL", oth.getCodeSolutionUrl());

        return othJson;
    }

    public static void main(String[] args)
    {
        Invitation i1 = new Invitation()
                .setCandidateEmail("candidate1@school.edu")
                .setCandidateFirstName("candidate")
                .setCandidateLastName("one")
                .setManagerEmail("manager@company.com")
                .setResumeUrl("http://yawpublic.io/resumes/candidate1.pdf");

        CodingProblem cp1 = new CodingProblem()
                .setName("problemName")
                .setLandingPageUrl("http://yawweb.io/codingproblem.html")
                .setGuid("problem1");

        OutputTestHistory oth1 = new OutputTestHistory()
                .setAttempts(10)
                .setCodeSolutionUrl("http://amazons3.com/studentsolution.zip");

        Invitation i2 = new Invitation()
                .setCandidateEmail("candidate1@school.edu")
                .setCandidateFirstName("candidate")
                .setCandidateLastName("two")
                .setManagerEmail("manager@company.com")
                .setResumeUrl("http://yawpublic.io/resumes/candidate2.pdf");
        CodingProblem cp2 = new CodingProblem()
                .setName("differentproblem")
                .setLandingPageUrl("http://yawweb.io/codingproblem2.html")
                .setGuid("anotherproblempage");
        OutputTestHistory oth2 = new OutputTestHistory()
                .setAttempts(10)
                .setCodeSolutionUrl("http://amazons3.com/studentsolution2.zip");

        CandidateWorkflow cwf1 = new CandidateWorkflow(i1, cp1, oth1);
        CandidateWorkflow cwf2 = new CandidateWorkflow(i2, cp2, oth2);

        ArrayList<CandidateWorkflow> interns = new ArrayList<>();
        interns.add(cwf1);
        interns.add(cwf2);

        LambdaHandlerGetInternCandidates foo = new LambdaHandlerGetInternCandidates();
        System.out.println(foo.buildInternListJason(interns));
    }

}
