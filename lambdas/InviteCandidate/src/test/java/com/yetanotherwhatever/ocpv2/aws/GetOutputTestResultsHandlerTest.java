package com.yetanotherwhatever.ocpv2.aws;

import com.yetanotherwhatever.ocpv2.OutputTestResultGetter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetOutputTestResultsHandlerTest {

    private JSONObject createEvent(String outputId) throws ParseException
    {

        JSONParser parser = new JSONParser();

        String event = "{\n" +
                "    \"resource\": \"Resource path\",\n" +
                "    \"path\": \"Path parameter\",\n" +
                "    \"httpMethod\": \"Incoming request's method name\",\n" +
                "    \"headers\": {},\n" +
                "    \"queryStringParameters\": {},\n" +
                (null == outputId ? "" : "    \"pathParameters\":  { \"outputid\": \"" + outputId + "\"},\n") +
                "    \"stageVariables\": {},\n" +
                "    \"requestContext\": {},\n" +
                "    \"body\": \"A JSON string of the request payload.\",\n" +
                "    \"isBase64Encoded\": \"A boolean flag to indicate if the applicable request payload is Base64-encode\"\n" +
                "}";

        JSONObject eventJ = (JSONObject) parser.parse(event);

        return eventJ;
    }

    @Test
    public void handler_resultsFound_success() throws ParseException, IOException
    {
        String outputId = "id";
        JSONObject event = createEvent(outputId);

        OutputTestResultGetter getter = new OutputTestResultGetter();
        DynamoOcpV2DB db = mock(DynamoOcpV2DB.class);
        OutputResults or = new OutputResults();
        or.setResults("expectedResults");
        when(db.getOutputResults(any(String.class))).thenReturn(or);    //results found
        getter.setDb(db);

        JSONObject responseJson = getter.getResults(event);

        assertThat(responseJson.get("statusCode"), is(equalTo("200")));
        JSONObject bodyJson = (JSONObject) responseJson.get("body");
        assertThat(bodyJson.get("result"), is(equalTo(or.getResults())));
    }

    @Test
    public void handler_resultsNotFound_404() throws ParseException, IOException
    {
        String outputId = "id";
        JSONObject event = createEvent(outputId);

        OutputTestResultGetter getter = new OutputTestResultGetter();
        DynamoOcpV2DB db = mock(DynamoOcpV2DB.class);
        when(db.getOutputResults(any(String.class))).thenReturn(null);    //results not found
        getter.setDb(db);

        JSONObject responseJson = getter.getResults(event);

        assertThat(responseJson.get("statusCode"), is(equalTo("404")));
    }


    @Test
    public void handler_invalidRequest_400() throws ParseException, IOException
    {
        JSONObject event = createEvent(null);

        OutputTestResultGetter getter = new OutputTestResultGetter();
        DynamoOcpV2DB db = mock(DynamoOcpV2DB.class);
        OutputResults or = new OutputResults();
        or.setResults("expectedResults");
        when(db.getOutputResults(any(String.class))).thenReturn(null);    //results found
        getter.setDb(db);

        JSONObject responseJson = getter.getResults(event);

        assertThat(responseJson.get("statusCode"), is(equalTo("400")));
    }
}
