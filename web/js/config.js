
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
var outputForm_policy;
var outputForm_signature;

var codeForm_success_action_redirect = "http://" + env + ".yetanotherwhatever.io/thanks.html";
var codeForm_policy;
var codeForm_signature;


switch(env) {
    
    //prod
    case "ocp":
        ocpv2RestAPIId = "0e5jqclkvb";
        
        var outputForm_policy = "eyJleHBpcmF0aW9uIjoiMjAyMC0wOS0yNlQwMDowMDowMFoiLCJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJvY3AudXBsb2FkLnlldGFub3RoZXJ3aGF0ZXZlci5pbyJ9LFsic3RhcnRzLXdpdGgiLCIka2V5IiwidXBsb2Fkcy9vdXRwdXQvIl0seyJhY2wiOiJwcml2YXRlIn0seyJzdWNjZXNzX2FjdGlvbl9yZWRpcmVjdCI6Imh0dHA6Ly9vY3AueWV0YW5vdGhlcndoYXRldmVyLmlvL3N1Ym1pdHRpbmcuaHRtbCJ9LHsieC1hbXotYWxnb3JpdGhtIjoiQVdTNC1ITUFDLVNIQTI1NiJ9LHsieC1hbXotY3JlZGVudGlhbCI6IkFLSUFJSFRRVlVKVVIySVdPQ09BLzIwMjAwOTI2L3VzLWVhc3QtMS9zMy9hd3M0X3JlcXVlc3QifSx7IngtYW16LWRhdGUiOiIyMDIwMDkyNlQwMDAwMDBaIn0seyJ4LWFtei1zdG9yYWdlLWNsYXNzIjoiUkVEVUNFRF9SRURVTkRBTkNZIn0sWyJzdGFydHMtd2l0aCIsIiR4LWFtei1tZXRhLWRhdGEiLCIiXSxbImNvbnRlbnQtbGVuZ3RoLXJhbmdlIiwwLDEwNDg1NzZdXX0=";
        var outputForm_signature = "4d50b3b8ec6d95c71cad96ef346c54dfa021b8b7cf063b85a67c98f7c98ef5af";

        var codeForm_policy = "eyJleHBpcmF0aW9uIjoiMjAyMC0wOS0yNlQwMDowMDowMFoiLCJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJvY3AudXBsb2FkLnlldGFub3RoZXJ3aGF0ZXZlci5pbyJ9LFsic3RhcnRzLXdpdGgiLCIka2V5IiwidXBsb2Fkcy9jb2RlLyJdLHsiYWNsIjoicHJpdmF0ZSJ9LHsic3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QiOiJodHRwOi8vb2NwLnlldGFub3RoZXJ3aGF0ZXZlci5pby90aGFua3MuaHRtbCJ9LHsieC1hbXotYWxnb3JpdGhtIjoiQVdTNC1ITUFDLVNIQTI1NiJ9LHsieC1hbXotY3JlZGVudGlhbCI6IkFLSUFJSFRRVlVKVVIySVdPQ09BLzIwMjAwOTI2L3VzLWVhc3QtMS9zMy9hd3M0X3JlcXVlc3QifSx7IngtYW16LWRhdGUiOiIyMDIwMDkyNlQwMDAwMDBaIn0seyJ4LWFtei1zdG9yYWdlLWNsYXNzIjoiUkVEVUNFRF9SRURVTkRBTkNZIn0sWyJzdGFydHMtd2l0aCIsIiR4LWFtei1tZXRhLWRhdGEiLCIiXSxbImNvbnRlbnQtbGVuZ3RoLXJhbmdlIiwwLDEwNDg1NzZdXX0=";
        var codeForm_signature = "a7925b0866df9d6d42cd2aa429110d28a9fbc236d0a24276a0ff7055a3b12fa3";
        break;
    //test 
    case "test":
        //fall through intentionally
    default:
        ocpv2RestAPIId = "rz34guof5h";
        
        var outputForm_policy= "eyJleHBpcmF0aW9uIjoiMjAyMC0wOS0yNlQwMDowMDowMFoiLCJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJ0ZXN0LnVwbG9hZC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8ifSxbInN0YXJ0cy13aXRoIiwiJGtleSIsInVwbG9hZHMvb3V0cHV0LyJdLHsiYWNsIjoicHJpdmF0ZSJ9LHsic3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QiOiJodHRwOi8vdGVzdC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8vc3VibWl0dGluZy5odG1sIn0seyJ4LWFtei1hbGdvcml0aG0iOiJBV1M0LUhNQUMtU0hBMjU2In0seyJ4LWFtei1jcmVkZW50aWFsIjoiQUtJQUlIVFFWVUpVUjJJV09DT0EvMjAyMDA5MjYvdXMtZWFzdC0xL3MzL2F3czRfcmVxdWVzdCJ9LHsieC1hbXotZGF0ZSI6IjIwMjAwOTI2VDAwMDAwMFoifSx7IngtYW16LXN0b3JhZ2UtY2xhc3MiOiJSRURVQ0VEX1JFRFVOREFOQ1kifSxbInN0YXJ0cy13aXRoIiwiJHgtYW16LW1ldGEtZGF0YSIsIiJdLFsiY29udGVudC1sZW5ndGgtcmFuZ2UiLDAsMTA0ODU3Nl1dfQ==";
        var outputForm_signature = "34d5e52945ab5ac973291ab1f5a0cc83392ab5ea9887dd43d3688ea5e3847ddc";

        var codeForm_policy = "eyJleHBpcmF0aW9uIjoiMjAyMC0wOS0yNlQwMDowMDowMFoiLCJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJ0ZXN0LnVwbG9hZC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8ifSxbInN0YXJ0cy13aXRoIiwiJGtleSIsInVwbG9hZHMvY29kZS8iXSx7ImFjbCI6InByaXZhdGUifSx7InN1Y2Nlc3NfYWN0aW9uX3JlZGlyZWN0IjoiaHR0cDovL3Rlc3QueWV0YW5vdGhlcndoYXRldmVyLmlvL3RoYW5rcy5odG1sIn0seyJ4LWFtei1hbGdvcml0aG0iOiJBV1M0LUhNQUMtU0hBMjU2In0seyJ4LWFtei1jcmVkZW50aWFsIjoiQUtJQUlIVFFWVUpVUjJJV09DT0EvMjAyMDA5MjYvdXMtZWFzdC0xL3MzL2F3czRfcmVxdWVzdCJ9LHsieC1hbXotZGF0ZSI6IjIwMjAwOTI2VDAwMDAwMFoifSx7IngtYW16LXN0b3JhZ2UtY2xhc3MiOiJSRURVQ0VEX1JFRFVOREFOQ1kifSxbInN0YXJ0cy13aXRoIiwiJHgtYW16LW1ldGEtZGF0YSIsIiJdLFsiY29udGVudC1sZW5ndGgtcmFuZ2UiLDAsMTA0ODU3Nl1dfQ==";
        var codeForm_signature = "38680e0cbf5814454ba2d478cfe267db60bececbd8c6a4fba7488d8a71eb1787";
}

//API gateway stage name matches env name
var stage = env;

//api gateway resource urls
var restAPIBaseURL = "https://" + ocpv2RestAPIId + ".execute-api.us-east-1.amazonaws.com/" + stage;
var inviteAPIURL = restAPIBaseURL + '/invitation';
var getRessultsAPIURL = restAPIBaseURL + '/outputtestresult';
