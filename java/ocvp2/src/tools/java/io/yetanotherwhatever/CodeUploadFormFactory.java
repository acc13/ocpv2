package io.yetanotherwhatever;

public class CodeUploadFormFactory {


    static private final String SUCCESS_REDIRECT_PAGE = "thanks.html";
    static private final String KEY_PREFIX = "uploads/code";
    static private final String ADDITIONAL_FIELDS = "\n" +
            "\t      <div class='formlabel'>File to upload: <input id='codeFile' name='file' type='file'> </div>\n" +
            "\t      <br> \n" +
            "\t      <div class='formlabel' id='emaildiv'>Your email address (preferably the same one as on your resume):\n" +
            "\t      <br>\n" +
            "\t      <input id='email' name='email' type='text'>\n" +
            "\t      <br><br></div>\n";
    static private final String FORM_ID = "codeForm";

    static public SignedS3Form buildCodeUploadForm(String awsAccessKeyID, String awsSecretAccessKey, String hostedZone, String stack)
    {
        return new SignedS3Form(awsAccessKeyID, awsSecretAccessKey, hostedZone, stack,
                FORM_ID, KEY_PREFIX, SUCCESS_REDIRECT_PAGE, ADDITIONAL_FIELDS);
    }

    static public String buildForm()
    {
        return SignedS3Form.buildForm(KEY_PREFIX, ADDITIONAL_FIELDS, FORM_ID);
    }
}
