'use strict';

/* Global Vars & Config */

//returns the name of the environment we are in
function extractStackName(url) {
    let hostname;
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

const env = extractStackName(window.location.href);

/*
 ****************** ENV SPECIFIC VARS  ****************** 
 */

//TODO fix this
let ocpv2RestAPIId = "rz34guof5h";

switch(env) {
        case 'test':
              ocpv2RestAPIId = "rz34guof5h";
              break;
        case 'ocp':
              ocpv2RestAPIId = "0e5jqclkvb";
              break;
}


/*************************** AUTO-GENERATED FORM SETUP CODE ***************************/
function prepareS3UploadForms() {
    switch(env) {
        case 'test':


              $('#outputForm').attr('action', 'http://test.upload.yetanotherwhatever.io.s3.amazonaws.com/');
              $('#outputForm input[name=success_action_redirect]').val('http://test.yetanotherwhatever.io/submitting.html');
            
              $('#outputForm input[name=x-amz-credential]').val('AKIAIHTQVUJUR2IWOCOA/20191001/us-east-1/s3/aws4_request');
              $('#outputForm input[name=x-amz-date]').val('20191001T000000Z');
            
              $('#outputForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0wMVQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOid0ZXN0LnVwbG9hZC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8nfSxbJ3N0YXJ0cy13aXRoJywnJGtleScsJ3VwbG9hZHMvb3V0cHV0LyddLHsnYWNsJzoncHJpdmF0ZSd9LHsnc3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QnOidodHRwOi8vdGVzdC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8vc3VibWl0dGluZy5odG1sJ30seyd4LWFtei1hbGdvcml0aG0nOidBV1M0LUhNQUMtU0hBMjU2J30seyd4LWFtei1jcmVkZW50aWFsJzonQUtJQUlIVFFWVUpVUjJJV09DT0EvMjAxOTEwMDEvdXMtZWFzdC0xL3MzL2F3czRfcmVxdWVzdCd9LHsneC1hbXotZGF0ZSc6JzIwMTkxMDAxVDAwMDAwMFonfSx7J3gtYW16LXN0b3JhZ2UtY2xhc3MnOidSRURVQ0VEX1JFRFVOREFOQ1knfSxbJ3N0YXJ0cy13aXRoJywnJHgtYW16LW1ldGEtZGF0YScsJyddLFsnY29udGVudC1sZW5ndGgtcmFuZ2UnLDAsMTA0ODU3Nl1dfQ==');
              $('#outputForm input[name=x-amz-signature]').val('dd5300923b50e223492e6f9a91c82eca9bc8bf26babd61405e14f05fa45562dd');

              $('#codeForm').attr('action', 'http://test.upload.yetanotherwhatever.io.s3.amazonaws.com/');
              $('#codeForm input[name=success_action_redirect]').val('http://test.yetanotherwhatever.io/thanks.html');
            
              $('#codeForm input[name=x-amz-credential]').val('AKIAIHTQVUJUR2IWOCOA/20191001/us-east-1/s3/aws4_request');
              $('#codeForm input[name=x-amz-date]').val('20191001T000000Z');
            
              $('#codeForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0wMVQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOid0ZXN0LnVwbG9hZC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8nfSxbJ3N0YXJ0cy13aXRoJywnJGtleScsJ3VwbG9hZHMvY29kZS8nXSx7J2FjbCc6J3ByaXZhdGUnfSx7J3N1Y2Nlc3NfYWN0aW9uX3JlZGlyZWN0JzonaHR0cDovL3Rlc3QueWV0YW5vdGhlcndoYXRldmVyLmlvL3RoYW5rcy5odG1sJ30seyd4LWFtei1hbGdvcml0aG0nOidBV1M0LUhNQUMtU0hBMjU2J30seyd4LWFtei1jcmVkZW50aWFsJzonQUtJQUlIVFFWVUpVUjJJV09DT0EvMjAxOTEwMDEvdXMtZWFzdC0xL3MzL2F3czRfcmVxdWVzdCd9LHsneC1hbXotZGF0ZSc6JzIwMTkxMDAxVDAwMDAwMFonfSx7J3gtYW16LXN0b3JhZ2UtY2xhc3MnOidSRURVQ0VEX1JFRFVOREFOQ1knfSxbJ3N0YXJ0cy13aXRoJywnJHgtYW16LW1ldGEtZGF0YScsJyddLFsnY29udGVudC1sZW5ndGgtcmFuZ2UnLDAsMTA0ODU3Nl1dfQ==');
              $('#codeForm input[name=x-amz-signature]').val('99d4f6970a7c86dde15000a252bcad554721a51fcd6016ccc90f9aa7a5220bac');
            break;
        case 'ocp':

              $('#outputForm').attr('action', 'http://ocp.upload.yetanotherwhatever.io.s3.amazonaws.com/');
              $('#outputForm input[name=success_action_redirect]').val('http://ocp.yetanotherwhatever.io/submitting.html');
            
              $('#outputForm input[name=x-amz-credential]').val('AKIAIHTQVUJUR2IWOCOA/20191001/us-east-1/s3/aws4_request');
              $('#outputForm input[name=x-amz-date]').val('20191001T000000Z');
            
              $('#outputForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0wMVQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOidvY3AudXBsb2FkLnlldGFub3RoZXJ3aGF0ZXZlci5pbyd9LFsnc3RhcnRzLXdpdGgnLCcka2V5JywndXBsb2Fkcy9vdXRwdXQvJ10seydhY2wnOidwcml2YXRlJ30seydzdWNjZXNzX2FjdGlvbl9yZWRpcmVjdCc6J2h0dHA6Ly9vY3AueWV0YW5vdGhlcndoYXRldmVyLmlvL3N1Ym1pdHRpbmcuaHRtbCd9LHsneC1hbXotYWxnb3JpdGhtJzonQVdTNC1ITUFDLVNIQTI1Nid9LHsneC1hbXotY3JlZGVudGlhbCc6J0FLSUFJSFRRVlVKVVIySVdPQ09BLzIwMTkxMDAxL3VzLWVhc3QtMS9zMy9hd3M0X3JlcXVlc3QnfSx7J3gtYW16LWRhdGUnOicyMDE5MTAwMVQwMDAwMDBaJ30seyd4LWFtei1zdG9yYWdlLWNsYXNzJzonUkVEVUNFRF9SRURVTkRBTkNZJ30sWydzdGFydHMtd2l0aCcsJyR4LWFtei1tZXRhLWRhdGEnLCcnXSxbJ2NvbnRlbnQtbGVuZ3RoLXJhbmdlJywwLDEwNDg1NzZdXX0=');
              $('#outputForm input[name=x-amz-signature]').val('39f20ba2558d96174c9a9143526ab40f16720c268c88be01cb8752dc3ea2f1e9');

              $('#codeForm').attr('action', 'http://ocp.upload.yetanotherwhatever.io.s3.amazonaws.com/');
              $('#codeForm input[name=success_action_redirect]').val('http://ocp.yetanotherwhatever.io/thanks.html');
            
              $('#codeForm input[name=x-amz-credential]').val('AKIAIHTQVUJUR2IWOCOA/20191001/us-east-1/s3/aws4_request');
              $('#codeForm input[name=x-amz-date]').val('20191001T000000Z');
            
              $('#codeForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0wMVQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOidvY3AudXBsb2FkLnlldGFub3RoZXJ3aGF0ZXZlci5pbyd9LFsnc3RhcnRzLXdpdGgnLCcka2V5JywndXBsb2Fkcy9jb2RlLyddLHsnYWNsJzoncHJpdmF0ZSd9LHsnc3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QnOidodHRwOi8vb2NwLnlldGFub3RoZXJ3aGF0ZXZlci5pby90aGFua3MuaHRtbCd9LHsneC1hbXotYWxnb3JpdGhtJzonQVdTNC1ITUFDLVNIQTI1Nid9LHsneC1hbXotY3JlZGVudGlhbCc6J0FLSUFJSFRRVlVKVVIySVdPQ09BLzIwMTkxMDAxL3VzLWVhc3QtMS9zMy9hd3M0X3JlcXVlc3QnfSx7J3gtYW16LWRhdGUnOicyMDE5MTAwMVQwMDAwMDBaJ30seyd4LWFtei1zdG9yYWdlLWNsYXNzJzonUkVEVUNFRF9SRURVTkRBTkNZJ30sWydzdGFydHMtd2l0aCcsJyR4LWFtei1tZXRhLWRhdGEnLCcnXSxbJ2NvbnRlbnQtbGVuZ3RoLXJhbmdlJywwLDEwNDg1NzZdXX0=');
              $('#codeForm input[name=x-amz-signature]').val('15fa3a3103ab652331c004bdea28ed717b549ea58eb39c107946c49a64bbaef6');
            break;
    }
}
/*************************** END AUTO-GENERATED FORM SETUP CODE ***************************/

//API gateway stage name matches env name
const stage = env;

//api gateway resource urls
const restAPIBaseURL = "https://" + ocpv2RestAPIId + ".execute-api.us-east-1.amazonaws.com/" + stage;
const inviteAPIURL = restAPIBaseURL + '/invitation';
const getRessultsAPIURL = restAPIBaseURL + '/outputtestresult';
