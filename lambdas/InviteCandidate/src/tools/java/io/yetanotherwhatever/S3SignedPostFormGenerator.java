package io.yetanotherwhatever;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.asList;





/*
This class was written based on articles:
http://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-authentication-HTTPPOST.html
http://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-HTTPPOSTConstructPolicy.html
http://docs.aws.amazon.com/AmazonS3/latest/API/sig-v4-authenticating-requests.html
http://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-UsingHTTPPOST.html
 */





public class S3SignedPostFormGenerator {

    public static void main(String[] args) {

        //PARSE ARGS

        String awsAccessKeyID = null, awsSecretAccessKey = null, hostedZone = null;
        ArrayList<String> stackNames  = null;

        try {
            awsAccessKeyID = args[0];
            awsSecretAccessKey = args[1];
            hostedZone = args[2];
            stackNames = new ArrayList<>(Arrays.asList(args[3].split(":")));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: " + System.getProperty("sun.java.command") +
                    " <access_key_id> <secret_access_key> <hosted_zone> <colon_delimited_stack_names>");
            System.exit(1);
        }

        generateHtml();

        generateJS(awsAccessKeyID, awsSecretAccessKey, hostedZone, stackNames);

    }

    private static void generateHtml()
    {
        //output "empty" forms
        System.out.println("\n\n<!----------------------- AUTO-GENERATED TEST OUTPUT FORM ----------------------->");
        System.out.println(new OutputTestFormFactory().buildForm());
        System.out.println("<!----------------------- END AUTO-GENERATED TEST OUTPUT FORM ----------------------->\n\n");


        System.out.println("\n\n<!----------------------- AUTO-GENERATED CODE UPLOAD FORM ----------------------->");
        System.out.println(new CodeUploadFormFactory().buildForm());
        System.out.println("<!----------------------- END AUTO-GENERATED CODE UPLOAD FORM ----------------------->\n\n");
    }


    private static void generateJS(String awsAccessKeyID, String awsSecretAccessKey, String hostedZone, ArrayList<String> stackNames)
    {

        System.out.println("<!--------------------- AUTO-GENERATED FORM SETUP JS --------------------->");
        System.out.println("function prepareS3UploadForms() {");
        System.out.println("\tswitch(env) {");


        for ( String stack : stackNames) {

            System.out.println("\t\tcase '" + stack + "':");

            SignedS3Form outputForm = OutputTestFormFactory.buildOutputTestForm(awsAccessKeyID, awsSecretAccessKey, hostedZone, stack);

            String updateOutputFormJS = "\n" +
                    "\t\t\t  $('#outputForm').attr('action', '" + outputForm.getAction() + "');\n" +
                    "\t\t\t  $('#outputForm input[name=success_action_redirect]').val('" + outputForm.getSuccessRedirect() + "');\n" +
                    "\t\t\t\n" +
                    "\t\t\t  $('#outputForm input[name=x-amz-credential]').val('" + outputForm.getCredential() + "');\n" +
                    "\t\t\t  $('#outputForm input[name=x-amz-date]').val('" + outputForm.getTimeStamp() + "');\n" +
                    "\t\t\t\n" +
                    "\t\t\t  $('#outputForm input[name=policy]').val('" + outputForm.getPolicy() + "');\n" +
                    "\t\t\t  $('#outputForm input[name=x-amz-signature]').val('" + outputForm.getSignedPolicy() + "');";

            SignedS3Form codeForm = CodeUploadFormFactory.buildCodeUploadForm(awsAccessKeyID, awsSecretAccessKey, hostedZone, stack);

            String updateCodeFormJS = "\n" +
                    "\t\t\t  $('#codeForm').attr('action', '" + codeForm.getAction() + "');\n" +
                    "\t\t\t  $('#codeForm input[name=success_action_redirect]').val('" + codeForm.getSuccessRedirect() + "');\n" +
                    "\t\t\t\n" +
                    "\t\t\t  $('#codeForm input[name=x-amz-credential]').val('" + codeForm.getCredential() + "');\n" +
                    "\t\t\t  $('#codeForm input[name=x-amz-date]').val('" + codeForm.getTimeStamp() + "');\n" +
                    "\t\t\t\n" +
                    "\t\t\t  $('#codeForm input[name=policy]').val('" + codeForm.getPolicy() + "');\n" +
                    "\t\t\t  $('#codeForm input[name=x-amz-signature]').val('" + codeForm.getSignedPolicy() + "');";

            System.out.println(updateOutputFormJS);
            System.out.println(updateCodeFormJS);

            System.out.println("\t\t\tbreak;");
        }

        System.out.println("\t}");  //close switch
        System.out.println("}");    //close function

        System.out.println("<!--------------------- END AUTO-GENERATED FORM SETUP CODE --------------------->");
    }




}
