'use strict';

/* Global Vars & Config */

//returns the name of the environment we are in
function extractStackName(url) {
    var hostname;
    //find & remove protocol (http, ftp, etc.) and get hostname

    if (url.indexOf("//") > -1) {
        hostname = url.split('/')[2];
    }
    else {
        hostname = url.split('/')[0];
    }

    //find & remove port number
    hostname = hostname.split(':')[0];
    //find & remove "?"
    hostname = hostname.split('?')[0];

    //ie. test.yetanotherwhatever.io
    return hostname.split('.')[0];
}

var env = extractStackName(window.location.href);

/*
 ****************** ENV SPECIFIC VARS  ****************** 
 */
var ocpv2RestAPIId = "";

var form_action = "http://" + env + ".upload.yetanotherwhatever.io.s3.amazonaws.com/";
var outputForm_success_action_redirect = "http://" + env + ".yetanotherwhatever.io/submitting.html";
var codeForm_success_action_redirect = "http://" + env + ".yetanotherwhatever.io/thanks.html";

switch(env) {
    
    //prod
    case "ocp":
        ocpv2RestAPIId = "0e5jqclkvb";
        
        var formCredential = "AKIAIHTQVUJUR2IWOCOA/20190929/us-east-1/s3/aws4_request";
        var formTimestamp = "20190929T000000Z";

        var outputForm_policy = "eyJleHBpcmF0aW9uIjoiMjAxOS0wOS0yOVQwMDowMDowMFoiLCJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJvY3AudXBsb2FkLnlldGFub3RoZXJ3aGF0ZXZlci5pbyJ9LFsic3RhcnRzLXdpdGgiLCIka2V5IiwidXBsb2Fkcy9vdXRwdXQvIl0seyJhY2wiOiJwcml2YXRlIn0seyJzdWNjZXNzX2FjdGlvbl9yZWRpcmVjdCI6Imh0dHA6Ly9vY3AueWV0YW5vdGhlcndoYXRldmVyLmlvL3N1Ym1pdHRpbmcuaHRtbCJ9LHsieC1hbXotYWxnb3JpdGhtIjoiQVdTNC1ITUFDLVNIQTI1NiJ9LHsieC1hbXotY3JlZGVudGlhbCI6IkFLSUFJSFRRVlVKVVIySVdPQ09BLzIwMTkwOTI5L3VzLWVhc3QtMS9zMy9hd3M0X3JlcXVlc3QifSx7IngtYW16LWRhdGUiOiIyMDE5MDkyOVQwMDAwMDBaIn0seyJ4LWFtei1zdG9yYWdlLWNsYXNzIjoiUkVEVUNFRF9SRURVTkRBTkNZIn0sWyJzdGFydHMtd2l0aCIsIiR4LWFtei1tZXRhLWRhdGEiLCIiXSxbImNvbnRlbnQtbGVuZ3RoLXJhbmdlIiwwLDEwNDg1NzZdXX0=";
        var outputForm_signature = "c81d652dc3f8546b431e8a8cb8060e7258314619d211829fb390346170cdb8e5";

        var codeForm_policy = "eyJleHBpcmF0aW9uIjoiMjAxOS0wOS0yOVQwMDowMDowMFoiLCJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJvY3AudXBsb2FkLnlldGFub3RoZXJ3aGF0ZXZlci5pbyJ9LFsic3RhcnRzLXdpdGgiLCIka2V5IiwidXBsb2Fkcy9jb2RlLyJdLHsiYWNsIjoicHJpdmF0ZSJ9LHsic3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QiOiJodHRwOi8vb2NwLnlldGFub3RoZXJ3aGF0ZXZlci5pby90aGFua3MuaHRtbCJ9LHsieC1hbXotYWxnb3JpdGhtIjoiQVdTNC1ITUFDLVNIQTI1NiJ9LHsieC1hbXotY3JlZGVudGlhbCI6IkFLSUFJSFRRVlVKVVIySVdPQ09BLzIwMTkwOTI5L3VzLWVhc3QtMS9zMy9hd3M0X3JlcXVlc3QifSx7IngtYW16LWRhdGUiOiIyMDE5MDkyOVQwMDAwMDBaIn0seyJ4LWFtei1zdG9yYWdlLWNsYXNzIjoiUkVEVUNFRF9SRURVTkRBTkNZIn0sWyJzdGFydHMtd2l0aCIsIiR4LWFtei1tZXRhLWRhdGEiLCIiXSxbImNvbnRlbnQtbGVuZ3RoLXJhbmdlIiwwLDEwNDg1NzZdXX0=";
        var codeForm_signature = "261f61441b7c7f2ff3c0ffc00c18a7a713093f4468b9171e4234fa9b025e8298";

        break;
    //test 
    case "test":
        //fall through intentionally
    default:
        ocpv2RestAPIId = "rz34guof5h";
        
        var formCredential = "AKIAIHTQVUJUR2IWOCOA/20190929/us-east-1/s3/aws4_request";
        var formTimestamp = "20190929T000000Z";

        var outputForm_policy = "eyJleHBpcmF0aW9uIjoiMjAxOS0wOS0yOVQwMDowMDowMFoiLCJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJ0ZXN0LnVwbG9hZC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8ifSxbInN0YXJ0cy13aXRoIiwiJGtleSIsInVwbG9hZHMvb3V0cHV0LyJdLHsiYWNsIjoicHJpdmF0ZSJ9LHsic3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QiOiJodHRwOi8vdGVzdC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8vc3VibWl0dGluZy5odG1sIn0seyJ4LWFtei1hbGdvcml0aG0iOiJBV1M0LUhNQUMtU0hBMjU2In0seyJ4LWFtei1jcmVkZW50aWFsIjoiQUtJQUlIVFFWVUpVUjJJV09DT0EvMjAxOTA5MjkvdXMtZWFzdC0xL3MzL2F3czRfcmVxdWVzdCJ9LHsieC1hbXotZGF0ZSI6IjIwMTkwOTI5VDAwMDAwMFoifSx7IngtYW16LXN0b3JhZ2UtY2xhc3MiOiJSRURVQ0VEX1JFRFVOREFOQ1kifSxbInN0YXJ0cy13aXRoIiwiJHgtYW16LW1ldGEtZGF0YSIsIiJdLFsiY29udGVudC1sZW5ndGgtcmFuZ2UiLDAsMTA0ODU3Nl1dfQ==";
        var outputForm_signature = "a0bca1776b374579464f04929251255a9c68632465b4aed565c5f0d218fff9e0";

        var codeForm_policy = "eyJleHBpcmF0aW9uIjoiMjAxOS0wOS0yOVQwMDowMDowMFoiLCJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJ0ZXN0LnVwbG9hZC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8ifSxbInN0YXJ0cy13aXRoIiwiJGtleSIsInVwbG9hZHMvY29kZS8iXSx7ImFjbCI6InByaXZhdGUifSx7InN1Y2Nlc3NfYWN0aW9uX3JlZGlyZWN0IjoiaHR0cDovL3Rlc3QueWV0YW5vdGhlcndoYXRldmVyLmlvL3RoYW5rcy5odG1sIn0seyJ4LWFtei1hbGdvcml0aG0iOiJBV1M0LUhNQUMtU0hBMjU2In0seyJ4LWFtei1jcmVkZW50aWFsIjoiQUtJQUlIVFFWVUpVUjJJV09DT0EvMjAxOTA5MjkvdXMtZWFzdC0xL3MzL2F3czRfcmVxdWVzdCJ9LHsieC1hbXotZGF0ZSI6IjIwMTkwOTI5VDAwMDAwMFoifSx7IngtYW16LXN0b3JhZ2UtY2xhc3MiOiJSRURVQ0VEX1JFRFVOREFOQ1kifSxbInN0YXJ0cy13aXRoIiwiJHgtYW16LW1ldGEtZGF0YSIsIiJdLFsiY29udGVudC1sZW5ndGgtcmFuZ2UiLDAsMTA0ODU3Nl1dfQ==";
        var codeForm_signature = "376202ed0742372b6789d1215d5b3b73e0c274cc55508d066ff45300883a6146";
}

//API gateway stage name matches env name
var stage = env;

//api gateway resource urls
var restAPIBaseURL = "https://" + ocpv2RestAPIId + ".execute-api.us-east-1.amazonaws.com/" + stage;
var inviteAPIURL = restAPIBaseURL + '/invitation';
var getRessultsAPIURL = restAPIBaseURL + '/outputtestresult';
