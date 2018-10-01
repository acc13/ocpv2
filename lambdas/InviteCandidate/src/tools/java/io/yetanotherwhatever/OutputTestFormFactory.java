package io.yetanotherwhatever;


public class OutputTestFormFactory {

    static private final String FORM_ID = "outputForm";
    static private final String KEY_PREFIX = "uploads/output";
    static private final String SUCCESS_REDIRECT_PAGE = "submitting.html";
    static private final String ADDITIONAL_FIELDS = "\n" +
            "      <div class='formlabel'>File to upload: <input id='outputFile' name='file' type='file'> </div>\n" +
            "      \n" +
            "      <br>\n";

    static public SignedS3Form buildOutputTestForm(String awsAccessKeyID, String awsSecretAccessKey, String hostedZone, String stack)
    {
        return new SignedS3Form(awsAccessKeyID, awsSecretAccessKey, hostedZone, stack,
                FORM_ID, KEY_PREFIX, SUCCESS_REDIRECT_PAGE, ADDITIONAL_FIELDS);
    }

    static public String buildForm()
    {
        return SignedS3Form.buildForm(KEY_PREFIX, ADDITIONAL_FIELDS, FORM_ID);
    }

}
