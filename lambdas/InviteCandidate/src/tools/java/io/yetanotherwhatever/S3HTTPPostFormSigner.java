package io.yetanotherwhatever;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;





/*
This class was written based on articles:
http://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-authentication-HTTPPOST.html
http://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-HTTPPOSTConstructPolicy.html
http://docs.aws.amazon.com/AmazonS3/latest/API/sig-v4-authenticating-requests.html
http://docs.aws.amazon.com/AmazonS3/latest/API/sigv4-UsingHTTPPOST.html
 */





public class S3HTTPPostFormSigner {

    String m_expiration;
    String m_dateStamp;
    String m_region;
    String m_serviceName;
    String m_algorithm;
    String m_accessKeyID;
    String m_aws_secret_key;
    String m_s3UploadBucketName;
    String m_s3WebBucketName;



    /*
    Generates two signed forms, for the online coding problem
    One to submit test output
    One to submit the final coded solution in a .zip file
     */
    S3HTTPPostFormSigner(String accessKeyID, String aws_secret_key, String hostedZone, String stackName)
    {
        initBucketNames(hostedZone, stackName);

        m_accessKeyID = accessKeyID;
        m_aws_secret_key = aws_secret_key;
        m_expiration = yearsFromToday(2);

        //WARNING: AWS oddity:
        //expiration will be calculate by S3 as max 7 days from x-amz-date
        //regardless of the value of the expiration field
        //so set x-amz-date in the future if you want a longer expiration period
        //else you will encounter policy expired 403 errors when you submit your form 7 days past
        m_dateStamp = m_expiration.replaceAll("-", "");

        m_region = "us-east-1";
        m_serviceName = "s3";
        m_algorithm = "AWS4-HMAC-SHA256";
    }

    private void initBucketNames(String hostedZone, String stackName) {

        m_s3WebBucketName = stackName + "." + hostedZone;
        m_s3UploadBucketName = stackName + ".upload." + hostedZone;
    }

    protected static String yearsFromToday(int i)
    {
        Calendar cal = Calendar.getInstance();
        return yearsFromToday(cal, i);
    }

    protected static String yearsFromToday(Calendar cal, int i)
    {
        cal.add(Calendar.YEAR, i); // to get previous year add 1
        Date d = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }

    static byte[] HmacSHA256(String data, byte[] key) throws Exception  {
        return HmacSHA256(data.getBytes("UTF-8"), key);
    }


    static byte[] HmacSHA256(byte[] data, byte[] key) throws Exception  {
        String algorithm="HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data);
    }

    /*
    based on http://docs.aws.amazon.com/general/latest/gr/sigv4-calculate-signature.html
     */
    static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception  {
        byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
        byte[] kDate    = HmacSHA256(dateStamp, kSecret);
        byte[] kRegion  = HmacSHA256(regionName, kDate);
        byte[] kService = HmacSHA256(serviceName, kRegion);
        byte[] kSigning = HmacSHA256("aws4_request", kService);
        return kSigning;
    }

    static String b64Encode(String in)
    {
        String b64 = "";

        in = in.replaceAll("\\s", "");
        try
        {
            b64 = Base64.getEncoder().encodeToString(in.getBytes("UTF-8"));

        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            System.exit(0);
        }

        return b64;
    }

    public static String signPolicy(String b64_policy_doc, String aws_secret_key, String dateStamp, String region, String serviceName)
    {
        try {

            byte[] signatureKey = getSignatureKey(aws_secret_key, dateStamp, region, serviceName);

            byte[] signingKey = signatureKey;

            byte[] hash = HmacSHA256(b64_policy_doc, signingKey);
            final StringBuilder builder = new StringBuilder();
            for(byte b : hash) {
                builder.append(String.format("%02x", b));
            }
            String signature = builder.toString();

            return signature;

        } catch(Exception e)
        {
            e.printStackTrace();

            System.exit(0);
        }

        //for compiler, never run
        return "";
    }

    private String buildPolicyDoc(String expiration,
                                         String folder,
                                         String algorithm,
                                         String accessKeyID,
                                         String dateStamp,
                                         String region,
                                         String serviceName,
                                         String redirectPage)
    {
        String policyDoc = "{\n" +
                "  \"expiration\":\"" + expiration + " T00:00:00Z\",\n" +
                "  \"conditions\": [\n" +
                "    {\"bucket\":\"" + m_s3UploadBucketName + "\"},\n" +
                "    [\"starts-with\",\"$key\",\"" + folder + "/\"],\n" +
                "    {\"acl\":\"private\"},\n" +
                "    {\"success_action_redirect\":\"http://" + m_s3WebBucketName + "/" + redirectPage + "\"},\n" +
                "    {\"x-amz-algorithm\":\"" + algorithm + "\"},\n" +
                "    {\"x-amz-credential\":\"" + accessKeyID + "/" + dateStamp + "/" + region + "/" + serviceName + "/aws4_request\"},\n" +
                "    {\"x-amz-date\":\"" + dateStamp + "T000000Z\"},\n" +
                "    {\"x-amz-storage-class\":\"REDUCED_REDUNDANCY\"},\n" +
                "    [\"starts-with\",\"$x-amz-meta-data\",\"\"],\n" +
                "    [\"content-length-range\",0,1048576]\n" +
                "  ]\n" +
                "}";
        System.out.println(policyDoc);

        return b64Encode(policyDoc);
    }

    private void buildForm(String folder, String additionalFields,
                           String redirectPage, String validation, String formId)
    {
        String policyDoc = buildPolicyDoc(m_expiration, folder, m_algorithm, m_accessKeyID, m_dateStamp, m_region, m_serviceName, redirectPage);
        String signedPolicy = signPolicy(policyDoc, m_aws_secret_key, m_dateStamp, m_region, m_serviceName);

        String form = "<form id=\"" + formId + "\" action=\"http://" + m_s3UploadBucketName + ".s3.amazonaws.com/\" method=\"post\"" +
                " enctype=\"multipart/form-data\" onsubmit=\"return(" + validation + ");\">\n" +
                "\t      <input type=\"hidden\" name=\"key\" value=\"" + folder + "/${filename}\">\n" +
                "\t      <input type=\"hidden\" name=\"acl\" value=\"private\"> \n" +
                "\t      <input type=\"hidden\" name=\"success_action_redirect\" value=\"http://" + m_s3WebBucketName + "/" + redirectPage + "\">\n" +
                "\t      <input type=\"hidden\" name=\"policy\" value='" + policyDoc + "'>\n" +
                "\t       <input type=\"hidden\" name=\"x-amz-algorithm\" value=\"" + m_algorithm + "\">\n" +
                "\t       <input type=\"hidden\" name=\"x-amz-credential\" value=\"" + m_accessKeyID + "/" + m_dateStamp + "/us-east-1/s3/aws4_request\">\n" +
                "\t       <input type=\"hidden\" name=\"x-amz-date\" value=\"" + m_dateStamp + "T000000Z\">\n" +
                "\t       <input type=\"hidden\" name=\"x-amz-storage-class\" value=\"REDUCED_REDUNDANCY\">\n" +
                "\t       <input type=\"hidden\" name=\"x-amz-signature\" value=\"" + signedPolicy + "\">\n" +
                "\t       <input type=\"hidden\" name=\"x-amz-meta-data\" value=\"\">\n" +
                "\t      <!-- Include any additional input fields here -->\n" +
                additionalFields +
                "\t      <input type=\"submit\" value=\"Upload File\">\n" +
                "    </form>";

        System.out.println();
        System.out.println("<!-- Replace form : " + formId  + "  -->");
        System.out.println(form);
        System.out.println();
        System.out.println();
    }


    void buildTestOutputForm()
    {
        //policy for uploading user's test output
        String htmlFormID = "outputForm";
        String s3KeyPrefix = "uploads/output";
        String successRedirectPage = "submitting.html";
        String additionalFormFields = "\n" +
                "      <div class=\"formlabel\">File to upload: <input id=\"outputFile\" name=\"file\" type=\"file\"> </div>\n" +
                "      \n" +
                "      <br>\n";
        String validationFunction = "genOutputKey()";

        buildForm(s3KeyPrefix, additionalFormFields, successRedirectPage, validationFunction, htmlFormID);
    }

    void buildUploadSolutionForm()
    {

        //policy for uploading user code (.zip)
        String successRedirectPage = "thanks.html";
        String s3KeyPrefix = "uploads/code";
        String additionalFormFields = "\n" +
                "\t      <div class=\"formlabel\">File to upload: <input id=\"codeFile\" name=\"file\" type=\"file\"> </div>\n" +
                "\t      <br> \n" +
                "\t      <div class=\"formlabel\" id=\"emaildiv\">Your email address (preferably the same one as on your resume):\n" +
                "\t      <br>\n" +
                "\t      <input id=\"email\" name=\"email\" type=\"text\">\n" +
                "\t      <br><br></div>\n";
        String validationFunction= "genCodeKey()";
        String htmlFormID = "codeForm";

        buildForm(s3KeyPrefix, additionalFormFields, successRedirectPage, validationFunction, htmlFormID);
    }

    public static void main(String[] args) {

        String awsAccessKeyID = null, awsSecretAccessKey = null, hostedZone = null, stackName = null;

        try {
            awsAccessKeyID = args[0];
            awsSecretAccessKey = args[1];
            hostedZone = args[2];
            stackName = args[3];
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.out.println("Usage: " + System.getProperty("sun.java.command") +
                    " access_key_id secret_access_key hosted_zone stack_name");
            System.exit(1);
        }


        S3HTTPPostFormSigner signer = new S3HTTPPostFormSigner(awsAccessKeyID, awsSecretAccessKey, hostedZone, stackName);

        signer.buildTestOutputForm();

        signer.buildUploadSolutionForm();

    }
}
