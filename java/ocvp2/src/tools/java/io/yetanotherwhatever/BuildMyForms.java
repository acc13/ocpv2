package io.yetanotherwhatever;

import java.util.ArrayList;
import java.util.Arrays;





/*
This class was written based on articles:
http://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-authentication-HTTPPOST.html
http://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-HTTPPOSTConstructPolicy.html
http://docs.aws.amazon.com/AmazonS3/latest/API/sig-v4-authenticating-requests.html
http://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-UsingHTTPPOST.html
 */





public class BuildMyForms {

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

        generateHtml(awsAccessKeyID, awsSecretAccessKey, hostedZone);

        generateJS(awsAccessKeyID, awsSecretAccessKey, hostedZone, stackNames);

    }

    private static void generateHtml(String awsAccessKeyID, String awsSecretAccessKey, String hostedZone)
    {

        //this is kind of hacky... but building the html templates doesn't require the stackName field
        String stackName = "";

        //output "empty" forms
        System.out.println(FormFactory.buildOutputTestForm(awsAccessKeyID, awsSecretAccessKey, hostedZone, stackName).buildFormHtml());

        System.out.println(FormFactory.buildCodeUploadForm(awsAccessKeyID, awsSecretAccessKey, hostedZone, stackName).buildFormHtml());

        System.out.println(FormFactory.buildInternshipRegistrationForm(awsAccessKeyID, awsSecretAccessKey, hostedZone, stackName).buildFormHtml());

    }



    private static String generateJsToUpdateSingleForm(SignedS3Form s3Form)
    {
        String formId = s3Form.getFormId();

        String updateFormJs = "\n" +
                "\t\t\tif ( $( \"#" + formId + "\" ).length )\t//if element exists\n" +
                "\t\t\t{\n" +
                "\t\t\t\t$('#" + formId + "').attr('action', '" + s3Form.getAction() + "');\n" +
                "\t\t\t\t$('#" + formId + " input[name=success_action_redirect]').val('" + s3Form.getSuccessRedirect() + "');\n" +
                "\t\t\t\n" +
                "\t\t\t\t$('#" + formId + " input[name=x-amz-credential]').val('" + s3Form.getCredential() + "');\n" +
                "\t\t\t\t$('#" + formId + " input[name=x-amz-date]').val('" + s3Form.getTimeStamp() + "');\n" +
                "\t\t\t\n" +
                "\t\t\t\t$('#" + formId + " input[name=policy]').val('" + s3Form.getPolicy() + "');\n" +
                "\t\t\t\t$('#" + formId + " input[name=x-amz-signature]').val('" + s3Form.getSignedPolicy() + "');\n" +
                "\t\t\t}" ;

        return updateFormJs;

    }

    private static void generateJS(String awsAccessKeyID, String awsSecretAccessKey, String hostedZone, ArrayList<String> stackNames)
    {

        System.out.println("/************************** AUTO-GENERATED FORM SETUP JS **************************/");
        System.out.println("function prepareS3UploadForms() {");
        System.out.println("\tswitch(env) {");


        for ( String stack : stackNames) {

            System.out.println("\t\tcase '" + stack + "':");

            SignedS3Form outputForm = FormFactory.buildOutputTestForm(awsAccessKeyID, awsSecretAccessKey, hostedZone, stack);
            String updateOutputFormJS = generateJsToUpdateSingleForm(outputForm);

            SignedS3Form codeForm = FormFactory.buildCodeUploadForm(awsAccessKeyID, awsSecretAccessKey, hostedZone, stack);
            String updateCodeFormJS = generateJsToUpdateSingleForm(codeForm);

            SignedS3Form internRegForm = FormFactory.buildInternshipRegistrationForm(awsAccessKeyID, awsSecretAccessKey, hostedZone, stack);
            String updateInternRegForm = generateJsToUpdateSingleForm(internRegForm);

            System.out.println(updateOutputFormJS);
            System.out.println(updateCodeFormJS);
            System.out.println(updateInternRegForm);

            System.out.println("\t\t\tbreak;");
        }

        System.out.println("\t}");  //close switch
        System.out.println("}");    //close function

        System.out.println("/************************** END AUTO-GENERATED FORM SETUP CODE **************************/");
    }




}