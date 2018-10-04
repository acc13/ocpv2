package io.yetanotherwhatever.ocpv2;


import io.yetanotherwhatever.ocpv2.aws.OutputResults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.io.IOException;


public class OutputTestResultGetter {

    public static final String HTTP_SUCCESS = "200";
    public static final String HTTP_INVALID = "400";
    public static final String HTTP_NOT_FOUND = "404";
    public static final String HTTP_SERVER_ERROR = "500";

    static final Logger logger = LogManager.getLogger(OutputTestResultGetter.class);

    IOcpV2DB db;

    public void setDb(IOcpV2DB db) {
        this.db = db;
    }

    /*
        event json object described here:
        https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html
     */
    public JSONObject getResults(JSONObject event) throws IllegalArgumentException
    {
        if (null == db)
        {
            throw new IllegalStateException("OutputTestResultGetter not properly initialized.");
        }

        String exception = null;
        String statusCode = null;
        String result = null;

        try
        {
            String uploadId = parseInputEvent(event);

            OutputResults or = db.getOutputResults(uploadId);
            if (null == or)
            {
                statusCode = HTTP_NOT_FOUND;
            }
            else {
                statusCode = HTTP_SUCCESS;
                result = or.getResults();
            }

        }
        catch (IOException e)
        {
            logger.error(e);
            statusCode = HTTP_SERVER_ERROR;
            exception = e.toString();
        }
        catch(IllegalArgumentException e)
        {
            logger.error(e);
            statusCode = HTTP_INVALID;
            exception = e.toString();
        }

        return buildResponseJson(statusCode, result, exception);
    }


    private String parseInputEvent(JSONObject event) throws IllegalArgumentException
    {

        JSONObject pps = (JSONObject)event.get("pathParameters");
        if (null == pps) {
            throw new IllegalArgumentException("pathParameters not found");
        }

        String outputId = (String)pps.get("outputid");
        if (null == outputId) {
            throw new IllegalArgumentException("outputid not found");
        }

        return outputId;
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
    static public JSONObject buildResponseJson(String statusCode, String result, String exception)
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
        bodyJson.put("result", result);

        responseJson.put("body", bodyJson.toString());

        if (null != exception) {
            responseJson.put("exception", exception);
        }

        return responseJson;
    }

}
