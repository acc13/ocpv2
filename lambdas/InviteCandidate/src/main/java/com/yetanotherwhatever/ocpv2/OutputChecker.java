package com.yetanotherwhatever.ocpv2;

import com.yetanotherwhatever.ocpv2.aws.OutputResults;
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
public class OutputChecker {

    static final Logger logger = LogManager.getLogger(OutputChecker.class);

    IFileStore store;
    IOcpV2DB ocpv2DB;

    String lastErr = "";

    public OutputChecker()
    {

    }

    public OutputChecker setOutputStore(IFileStore store)
    {
        this.store = store;
        return this;
    }

    public OutputChecker setDB(IOcpV2DB db)
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
        or.setResults(success? "Success!" : lastErr);
        or.setUploadID(uploadId);
        ocpv2DB.write(or);

        ocpv2DB.updateInvitation(invitationId, or.getUploadDate(), success);

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
                lastErr = "Failure on line: " + line + "\n" +
                        "Expected line \"" + expected +"\" but encountered line \"" + test + "\"";
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
