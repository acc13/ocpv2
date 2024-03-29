package io.yetanotherwhatever.ocpv2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Created by achang on 9/12/2018.
 */
public class OutputTester {

    static final Logger logger = LogManager.getLogger(OutputTester.class);

    IFileStore store;
    IOcpV2DB ocpv2DB;

    String lastErr = "";

    public OutputTester()
    {

    }

    public OutputTester setOutputStore(IFileStore store)
    {
        this.store = store;
        return this;
    }

    public OutputTester setDB(IOcpV2DB db)
    {
        this.ocpv2DB = db;
        return this;
    }

    public void checkOutput(String invitationId, String uploadId, String testFileName, String expectedFileName) throws IOException
    {
        //retrieve and compare test vs. expected output
        InputStream test = store.readFile(testFileName);
        InputStream expected = store.readFile(expectedFileName);

        boolean success = doStreamsMatch(test, expected);


        //save results
        OutputResults or = new OutputResults();
        or.setInvitationId(invitationId);
        or.setUploadDate(new Date());
        //TODO - add unit tests to confirm that success/fail string expected by web page js is contained in or.results
        or.setResults(success? "Your output is correct!  Please return to your problem page and follow the instructions in the section, 'Submitting Your Solution.'" : lastErr);
        or.setUploadID(uploadId);
        ocpv2DB.write(or);

        ocpv2DB.updateOutputTestHistory(invitationId, or.getUploadDate(), success);

    }

    //returns


    boolean doStreamsMatch(InputStream testOutput, InputStream expectedOutput) throws IOException
    {

        BufferedReader testBR = new BufferedReader(new InputStreamReader(testOutput));
        BufferedReader excpectedBR = new BufferedReader(new InputStreamReader(expectedOutput));

        String test;
        String expected;
        int line = 1;
        while((expected = excpectedBR.readLine()) != null) {

            test = testBR.readLine();

            if(null == test)
            {
                //output too short
                lastErr = "Output file too short.";
                return false;
            }

            test = test.trim();
            expected = expected.trim();

            if (!test.equals(expected))
            {
                lastErr = "Output incorrect.\n" +
                        "First error countered on line " + line + ".\n" +
                        "Expected value \"" + expected +"\" " +
                        (expected.length()==0? "(empty string) " : "") +
                        "but encountered value \"" + test + "\" "+
                        (test.length()==0? "(empty string) " : "");

                String friendlyHint = "\n\n\nSuggestions: \n" +
                        "-Did you remember to remove trailing and preceding whitespace?\n" +
                        "-Does your solution work for the sample input and output provided?";

                lastErr += friendlyHint;

                //output doesn't match
                return false;
            }

            line++;
        }

        if (testBR.readLine() != null)
        {
            //output too long
            lastErr = "Output file too long.";
            return false;
        }

        return true;
    }


}
