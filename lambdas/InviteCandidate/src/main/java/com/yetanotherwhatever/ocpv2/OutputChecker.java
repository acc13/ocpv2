package com.yetanotherwhatever.ocpv2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by achang on 9/12/2018.
 */
public class OutputChecker {

    IOutputStore store;
    IOcpV2DB ocpv2DB;

    String lastErr = "";

    OutputChecker()
    {

    }

    OutputChecker setOutputStore(IOutputStore store)
    {
        this.store = store;
        return this;
    }

    OutputChecker setDB(IOcpV2DB db)
    {
        this.ocpv2DB = db;
        return this;
    }

    public void checkOutput() throws IOException
    {
        //retrieve and compare test vs. expected output
        InputStream test = store.getTestOutput();
        InputStream expected = store.getExpectedOutput();

        boolean success = doStreamsMatch(test, expected);

        //build results page

        //save success

    }

    //returns
    static boolean doStreamsMatch(InputStream testOutput, InputStream expectedOutput) throws IOException
    {
        BufferedReader testBR = new BufferedReader(new InputStreamReader(testOutput));
        BufferedReader excpectedBR = new BufferedReader(new InputStreamReader(expectedOutput));

        String line;
        while((line = testBR.readLine()) != null) {
            System.out.println(line);
        }

        return false;
    }


}
