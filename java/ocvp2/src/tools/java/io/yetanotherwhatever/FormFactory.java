package io.yetanotherwhatever;

public class FormFactory {

    static public SignedS3Form buildCodeUploadForm(String awsAccessKeyID, String awsSecretAccessKey, String hostedZone, String stack)
    {

        final String SUCCESS_REDIRECT_PAGE = "thanks.html";
        final String KEY_PREFIX = "uploads/code";
        final String ADDITIONAL_FIELDS = "\n" +
                "\t      <div class='formlabel'>File to upload: <input id='codeFile' name='file' type='file'> </div>\n" +
                "\t      <br> \n" +
                "\t      <div class='formlabel' id='emaildiv'>Your email address (preferably the same one as on your resume):\n" +
                "\t      <br>\n" +
                "\t      <input id='email' name='email' type='text'>\n" +
                "\t      <br><br></div>\n";
        final String FORM_ID = "codeForm";

        return new SignedS3Form(awsAccessKeyID, awsSecretAccessKey, hostedZone, stack,
                FORM_ID, KEY_PREFIX, SUCCESS_REDIRECT_PAGE, ADDITIONAL_FIELDS);
    }


    static public SignedS3Form buildOutputTestForm(String awsAccessKeyID, String awsSecretAccessKey, String hostedZone, String stack)
    {
        final String FORM_ID = "outputForm";
        final String KEY_PREFIX = "uploads/output";
        final String SUCCESS_REDIRECT_PAGE = "submitting.html";
        final String ADDITIONAL_FIELDS = "\n" +
                "      <div class='formlabel'>File to upload: <input id='outputFile' name='file' type='file'> </div>\n" +
                "      \n" +
                "      <br>\n";
        return new SignedS3Form(awsAccessKeyID, awsSecretAccessKey, hostedZone, stack,
                FORM_ID, KEY_PREFIX, SUCCESS_REDIRECT_PAGE, ADDITIONAL_FIELDS);
    }


    static public SignedS3Form buildInternshipRegistrationForm(String awsAccessKeyID, String awsSecretAccessKey, String hostedZone, String stack)
    {
        final String FORM_ID = "internRegForm";
        final String KEY_PREFIX = "uploads/internshipRegistration";
        final String SUCCESS_REDIRECT_PAGE = "registrationComplete.html";
        final String ADDITIONAL_FIELDS = "\n" +
                "\t\t<p>\n" +
                "\t\t  <label for=\"first-name\">First name:</label>\n" +
                "\t\t  <input type=\"text\" id=\"first-name\" placeholder=\"first name...\" required/>\n" +
                "\t\t </p>\n" +
                "\n" +
                "\t\t<p>\n" +
                "\t\t  <label for=\"last-name\">Last name:</label>\n" +
                "\t\t  <input type=\"text\" id=\"last-name\" placeholder=\"last name...\" required/>\n" +
                "\t\t </p>\n" +
                "\n" +
                "\t\t<p>\t \n" +
                "\t\t  <label for=\"email\">Email:</label>\n" +
                "\t\t  <input class=email type=\"email\" id=\"email\" placeholder=\"email@myuniversity.edu\" required/>\n" +
                "\t\t</p>\n" +
                "\t\t<div class='formlabel'>Resume: <input id='resumeFile' name='file' type='file' required> </div>\n" +
                "\t\t<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" +
                "\t\t  <input type=\"checkbox\" id=\"full_time_student\"/>\n" +
                "\t\t  <label for=\"full_time_student\">I will be enrolled as a full-time student in Fall of 2019.</label>\n" +
                "\t\t</p>\n" +
                "\t\t<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" +
                "\t\t  <input type=\"checkbox\" id=\"available_2020\"/>\n" +
                "\t\t  <label for=\"available_2020\">I will be available to work full-time in 2020.</label>\n" +
                "\t\t</p><br>\n";

        return new SignedS3Form(awsAccessKeyID, awsSecretAccessKey, hostedZone, stack,
                FORM_ID, KEY_PREFIX, SUCCESS_REDIRECT_PAGE, ADDITIONAL_FIELDS);
    }
}
