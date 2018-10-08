package io.yetanotherwhatever;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class SignedS3Form
{

    private final String awsAccessKeyID, awsSecretAccessKey, hostedZone, stack, s3WebBucketName, s3UploadBucketName;

    private final String expiration;
    private final String dateStamp;

    private final String formId;
    private final String keyPrefix;
    private final String successRedirectPage;
    private final String additionalFields;


    public SignedS3Form(String awsAccessKeyID, String awsSecretAccessKey, String hostedZone, String stack,
                        String formId, String keyPrefix, String successRedirectPage, String additionalFields) {



        this.awsAccessKeyID = awsAccessKeyID;
        this.awsSecretAccessKey = awsSecretAccessKey;
        this.hostedZone = hostedZone;
        this.stack = stack;

        this.expiration = yearsFromToday(1);
        //WARNING: AWS oddity:
        //expiration will be calculate by S3 as max 7 days from x-amz-date
        //regardless of the value of the expiration field
        //so set x-amz-date in the future if you want a longer expiration period
        //else you will encounter policy expired 403 errors when you submit your form 7 days past
        this.dateStamp = expiration.replaceAll("-", "");


        this.s3WebBucketName = stack + "." + hostedZone;
        this.s3UploadBucketName = stack + ".upload." + hostedZone;

        this.formId = formId;
        this.keyPrefix = keyPrefix;
        this.successRedirectPage = "http://" + s3WebBucketName + "/" + successRedirectPage;
        this.additionalFields = additionalFields;

    }

    public String getCredential() {
        return awsAccessKeyID + "/" + dateStamp + "/us-east-1/s3/aws4_request";
    }

    public String getTimeStamp() {
        return this.dateStamp + "T000000Z";
    }

    public String getAction() {
        return "http://" + s3UploadBucketName + ".s3.amazonaws.com/";
    }

    public String getSuccessRedirect() {
        return successRedirectPage;
    }

    public String getPolicy() {
        return buildPolicyDoc(keyPrefix, successRedirectPage);
    }

    public String getSignedPolicy() {
        return sign(getPolicy(), awsSecretAccessKey, dateStamp);
    }

    public String getFormId() { return formId; }




    static final String ALGORITHM = "AWS4-HMAC-SHA256";
    static final String REGION = "us-east-1";
    static final String SERVICE_NAME = "s3";


    protected String buildFormHtml()
    {
        String validation = "prepare"+formId + "()";
        //TBD is stuff that is replaced on a per env (and signing expiry) basis
        String form = "<!-- ************************** AUTO-GENERATED UPLOAD FORM \"" + formId + "\" ************************** -->\n" +
                "<form id='" + formId + "' action='FFFFFFFFFFFF' method='post'" +
                " enctype='multipart/form-data' onsubmit='return(" + validation + ");'>\n" +
                "\t      <input type='hidden' name='key' value='" + keyPrefix + "/${filename}'>\n" +
                "\t      <input type='hidden' name='acl' value='private'> \n" +
                "\t      <input type='hidden' name='success_action_redirect' value='FFFFFFFFFFFF'>\n" +
                "\t      <input type='hidden' name='policy' value='FFFFFFFFFFFF'>\n" +
                "\t       <input type='hidden' name='x-amz-algorithm' value='" + ALGORITHM + "'>\n" +
                "\t       <input type='hidden' name='x-amz-credential' value='FFFFFFFFFFFF'>\n" +
                "\t       <input type='hidden' name='x-amz-date' value='FFFFFFFFFFFF'>\n" +
                "\t       <input type='hidden' name='x-amz-storage-class' value='REDUCED_REDUNDANCY'>\n" +
                "\t       <input type='hidden' name='x-amz-signature' value='FFFFFFFFFFFF'>\n" +
                "\t       <input type='hidden' name='x-amz-meta-data' value=''>\n\n" +
                "\t      <!-- ADDITIONAL INPUT FIELDS -->\n" +
                additionalFields + "\n\n" +
                "\t      <!-- END ADDITIONAL INPUT FIELDS -->\n" +
                "\t      <input type='submit' value='Submit'>\n" +
                "    </form>\n" +
                "<!-- ************************** END AUTO-GENERATED UPLOAD FORM \"" + formId + "\" ************************** -->\n";

        return form;
    }

    private String buildPolicyDoc(String s3KeyPrefix,
                                        String successRedirectPage)
    {
        String policyDoc = "{\n" +
                "  'expiration':'" + expiration + " T00:00:00Z',\n" +
                "  'conditions': [\n" +
                "    {'bucket':'" + s3UploadBucketName + "'},\n" +
                "    ['starts-with','$key','" + s3KeyPrefix + "/'],\n" +
                "    {'acl':'private'},\n" +
                "    {'success_action_redirect':'" + successRedirectPage + "'},\n" +
                "    {'x-amz-algorithm':'" + ALGORITHM + "'},\n" +
                "    {'x-amz-credential':'" + awsAccessKeyID + "/" + dateStamp + "/" + REGION + "/" + SERVICE_NAME + "/aws4_request'},\n" +
                "    {'x-amz-date':'" + dateStamp + "T000000Z'},\n" +
                "    {'x-amz-storage-class':'REDUCED_REDUNDANCY'},\n" +
                "    ['starts-with','$x-amz-meta-data',''],\n" +
                "    ['content-length-range',0,1048576]\n" +
                "  ]\n" +
                "}";

        return b64Encode(policyDoc);
    }



    protected static String b64Encode(String in)
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

    private static  String sign(String b64_doc, String secret, String dateStamp)
    {
        try {

            byte[] signatureKey = buildSignatureKey(secret, dateStamp, REGION, SERVICE_NAME);

            byte[] signingKey = signatureKey;

            byte[] hash = HmacSHA256(b64_doc, signingKey);
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



    private static byte[] HmacSHA256(String data, byte[] key) throws Exception  {
        byte[] bytes = data.getBytes("UTF-8");
        String algorithm="HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(bytes);
    }


    /*
    based on http://docs.aws.amazon.com/general/latest/gr/sigv4-calculate-signature.html
     */
    private static byte[] buildSignatureKey(String secret, String dateStamp, String regionName, String serviceName) throws Exception  {
        byte[] kSecret = ("AWS4" + secret).getBytes("UTF8");
        byte[] kDate    = HmacSHA256(dateStamp, kSecret);
        byte[] kRegion  = HmacSHA256(regionName, kDate);
        byte[] kService = HmacSHA256(serviceName, kRegion);
        byte[] kSigning = HmacSHA256("aws4_request", kService);
        return kSigning;
    }

    private static String yearsFromToday(int i)
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
}
