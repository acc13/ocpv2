package com.yetanotherwhatever.ocpv2.aws;

import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.yetanotherwhatever.ocpv2.OutputTestResultGetter;
import static com.yetanotherwhatever.ocpv2.OutputTestResultGetter.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;


//using AWS_PROXY Api Gateway->Lambda integration
//see: https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-create-api-as-simple-proxy-for-lambda.html#api-gateway-proxy-integration-lambda-function-java


public class GetOutputTestResultsHandler implements RequestStreamHandler {

    static final Logger logger = LogManager.getLogger(GetOutputTestResultsHandler.class);

    JSONParser parser = new JSONParser();

    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException
    {
        JSONObject responseJson = null;

        try {
            JSONObject event = getEvent(inputStream);
            logger.debug(event.toJSONString());

            OutputTestResultGetter resultsGetter = new OutputTestResultGetter();
            resultsGetter.setDb(new DynamoOcpV2DB());

            responseJson = resultsGetter.getResults(event);

        }
        catch (ParseException e)
        {
            logger.error(e);
            responseJson = buildResponseJson(HTTP_INVALID, null, e.toString());
        }

        logger.debug(responseJson.toJSONString());

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toJSONString());
        writer.close();
    }

    //for unit test mocking
    protected JSONObject getEvent(InputStream inputStream) throws ParseException, IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject event = (JSONObject)parser.parse(reader);

        reader.close();

        return event;
    }

}
