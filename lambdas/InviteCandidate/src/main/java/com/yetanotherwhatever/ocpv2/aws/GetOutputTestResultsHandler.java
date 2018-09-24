package com.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.yetanotherwhatever.ocpv2.IOcpV2DB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;


//using AWS_PROXY Api Gateway->Lambda integration
//see: https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-create-api-as-simple-proxy-for-lambda.html#api-gateway-proxy-integration-lambda-function-java


public class GetOutputTestResultsHandler implements RequestStreamHandler {
    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(GetOutputTestResultsHandler.class);

    JSONParser parser = new JSONParser();


    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException
    {

        logger.debug("Loading Java Lambda handler of ProxyWithStream");


        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();
        String uploadId = null;
        String responseCode = "200";

        JSONObject responseBody = new JSONObject();

        try {

            JSONObject event = (JSONObject)parser.parse(reader);
            //responseBody.put("input", event.toJSONString());

            if (event.get("pathParameters") == null) {
                throw new IllegalArgumentException("pathParameters not found");
            }
            JSONObject pps = (JSONObject)event.get("pathParameters");

            if (null == pps.get("outputid")) {
                throw new IllegalArgumentException("outputid not found");
            }

            uploadId = (String)pps.get("outputid");

            IOcpV2DB db = new DynamoOcpV2DB();
            OutputResults or = db.getOutputResults(uploadId);
            if (null == or)
            {
                responseCode = "404";
            }
            else {
                responseCode = "200";
                responseBody.put("result", or.getResults());
            }

        }
        catch (IllegalArgumentException | ParseException e)
        {
            logger.error(e);
            responseCode = "400";
            responseJson.put("exception", e);
        }
        catch (IOException e)
        {
            logger.error(e);
            responseCode = "500";
            responseJson.put("exception", e);
        }

        //for response format, see:
        ////https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html
        JSONObject headerJson = new JSONObject();
        headerJson.put("Access-Control-Allow-Origin:domain-name", "*");

        responseJson.put("isBase64Encoded", false);
        responseJson.put("statusCode", responseCode);
        responseJson.put("headers", headerJson);
        responseJson.put("body", responseBody.toString());

        logger.debug(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toJSONString());
        writer.close();
    }
}
