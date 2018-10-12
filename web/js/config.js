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

//TODO fix this so it pulls from cloudformation stack output
let ocpv2RestAPIId = "rz34guof5h";

switch(env) {
        case 'test':
              ocpv2RestAPIId = "rz34guof5h";
              break;
        case 'ocp':
              ocpv2RestAPIId = "0e5jqclkvb";
              break;
}


/************************** AUTO-GENERATED FORM SETUP JS **************************/
function prepareS3UploadForms() {
  switch(env) {
    case 'test':

      if ( $( "#outputForm" ).length )  //if element exists
      {
        $('#outputForm').attr('action', 'http://test.upload.yetanotherwhatever.io.s3.amazonaws.com/');
        $('#outputForm input[name=success_action_redirect]').val('http://test.yetanotherwhatever.io/submitting.html');
      
        $('#outputForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191011/us-east-1/s3/aws4_request');
        $('#outputForm input[name=x-amz-date]').val('20191011T000000Z');
      
        $('#outputForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMVQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOid0ZXN0LnVwbG9hZC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8nfSxbJ3N0YXJ0cy13aXRoJywnJGtleScsJ3VwbG9hZHMvb3V0cHV0LyddLHsnYWNsJzoncHJpdmF0ZSd9LHsnc3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QnOidodHRwOi8vdGVzdC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8vc3VibWl0dGluZy5odG1sJ30seyd4LWFtei1hbGdvcml0aG0nOidBV1M0LUhNQUMtU0hBMjU2J30seyd4LWFtei1jcmVkZW50aWFsJzonQUtJQUpFRU9GTkVFV1BHQVpNNUEvMjAxOTEwMTEvdXMtZWFzdC0xL3MzL2F3czRfcmVxdWVzdCd9LHsneC1hbXotZGF0ZSc6JzIwMTkxMDExVDAwMDAwMFonfSx7J3gtYW16LXN0b3JhZ2UtY2xhc3MnOidSRURVQ0VEX1JFRFVOREFOQ1knfSxbJ3N0YXJ0cy13aXRoJywnJHgtYW16LW1ldGEtZGF0YScsJyddLFsnY29udGVudC1sZW5ndGgtcmFuZ2UnLDAsMTA0ODU3Nl1dfQ==');
        $('#outputForm input[name=x-amz-signature]').val('9101c4e9b7e2384ddaeeeb495fd871bb995e18af330e7dbf9cd663380ab490ce');
      }

      if ( $( "#codeForm" ).length )  //if element exists
      {
        $('#codeForm').attr('action', 'http://test.upload.yetanotherwhatever.io.s3.amazonaws.com/');
        $('#codeForm input[name=success_action_redirect]').val('http://test.yetanotherwhatever.io/thanks.html');
      
        $('#codeForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191011/us-east-1/s3/aws4_request');
        $('#codeForm input[name=x-amz-date]').val('20191011T000000Z');
      
        $('#codeForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMVQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOid0ZXN0LnVwbG9hZC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8nfSxbJ3N0YXJ0cy13aXRoJywnJGtleScsJ3VwbG9hZHMvY29kZS8nXSx7J2FjbCc6J3ByaXZhdGUnfSx7J3N1Y2Nlc3NfYWN0aW9uX3JlZGlyZWN0JzonaHR0cDovL3Rlc3QueWV0YW5vdGhlcndoYXRldmVyLmlvL3RoYW5rcy5odG1sJ30seyd4LWFtei1hbGdvcml0aG0nOidBV1M0LUhNQUMtU0hBMjU2J30seyd4LWFtei1jcmVkZW50aWFsJzonQUtJQUpFRU9GTkVFV1BHQVpNNUEvMjAxOTEwMTEvdXMtZWFzdC0xL3MzL2F3czRfcmVxdWVzdCd9LHsneC1hbXotZGF0ZSc6JzIwMTkxMDExVDAwMDAwMFonfSx7J3gtYW16LXN0b3JhZ2UtY2xhc3MnOidSRURVQ0VEX1JFRFVOREFOQ1knfSxbJ3N0YXJ0cy13aXRoJywnJHgtYW16LW1ldGEtZGF0YScsJyddLFsnY29udGVudC1sZW5ndGgtcmFuZ2UnLDAsMTA0ODU3Nl1dfQ==');
        $('#codeForm input[name=x-amz-signature]').val('323d083316cfd2fbb840cfa3d68365bf2d0de1a807d3465a964d9aa40411185a');
      }

      if ( $( "#internRegForm" ).length ) //if element exists
      {
        $('#internRegForm').attr('action', 'http://test.upload.yetanotherwhatever.io.s3.amazonaws.com/');
        $('#internRegForm input[name=success_action_redirect]').val('http://test.yetanotherwhatever.io/registration2019complete.html');
      
        $('#internRegForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191011/us-east-1/s3/aws4_request');
        $('#internRegForm input[name=x-amz-date]').val('20191011T000000Z');
      
        $('#internRegForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMVQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOid0ZXN0LnVwbG9hZC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8nfSxbJ3N0YXJ0cy13aXRoJywnJGtleScsJ3VwbG9hZHMvaW50ZXJuc2hpcFJlZ2lzdHJhdGlvbi8nXSx7J2FjbCc6J3ByaXZhdGUnfSx7J3N1Y2Nlc3NfYWN0aW9uX3JlZGlyZWN0JzonaHR0cDovL3Rlc3QueWV0YW5vdGhlcndoYXRldmVyLmlvL3JlZ2lzdHJhdGlvbjIwMTljb21wbGV0ZS5odG1sJ30seyd4LWFtei1hbGdvcml0aG0nOidBV1M0LUhNQUMtU0hBMjU2J30seyd4LWFtei1jcmVkZW50aWFsJzonQUtJQUpFRU9GTkVFV1BHQVpNNUEvMjAxOTEwMTEvdXMtZWFzdC0xL3MzL2F3czRfcmVxdWVzdCd9LHsneC1hbXotZGF0ZSc6JzIwMTkxMDExVDAwMDAwMFonfSx7J3gtYW16LXN0b3JhZ2UtY2xhc3MnOidSRURVQ0VEX1JFRFVOREFOQ1knfSxbJ3N0YXJ0cy13aXRoJywnJHgtYW16LW1ldGEtZGF0YScsJyddLFsnY29udGVudC1sZW5ndGgtcmFuZ2UnLDAsMTA0ODU3Nl1dfQ==');
        $('#internRegForm input[name=x-amz-signature]').val('13ad19f76d496083bae4697230597dfede662829ca12ba0e01dbb767cda551d7');
      }
      break;
    case 'ocp':

      if ( $( "#outputForm" ).length )  //if element exists
      {
        $('#outputForm').attr('action', 'http://ocp.upload.yetanotherwhatever.io.s3.amazonaws.com/');
        $('#outputForm input[name=success_action_redirect]').val('http://ocp.yetanotherwhatever.io/submitting.html');
      
        $('#outputForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191011/us-east-1/s3/aws4_request');
        $('#outputForm input[name=x-amz-date]').val('20191011T000000Z');
      
        $('#outputForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMVQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOidvY3AudXBsb2FkLnlldGFub3RoZXJ3aGF0ZXZlci5pbyd9LFsnc3RhcnRzLXdpdGgnLCcka2V5JywndXBsb2Fkcy9vdXRwdXQvJ10seydhY2wnOidwcml2YXRlJ30seydzdWNjZXNzX2FjdGlvbl9yZWRpcmVjdCc6J2h0dHA6Ly9vY3AueWV0YW5vdGhlcndoYXRldmVyLmlvL3N1Ym1pdHRpbmcuaHRtbCd9LHsneC1hbXotYWxnb3JpdGhtJzonQVdTNC1ITUFDLVNIQTI1Nid9LHsneC1hbXotY3JlZGVudGlhbCc6J0FLSUFKRUVPRk5FRVdQR0FaTTVBLzIwMTkxMDExL3VzLWVhc3QtMS9zMy9hd3M0X3JlcXVlc3QnfSx7J3gtYW16LWRhdGUnOicyMDE5MTAxMVQwMDAwMDBaJ30seyd4LWFtei1zdG9yYWdlLWNsYXNzJzonUkVEVUNFRF9SRURVTkRBTkNZJ30sWydzdGFydHMtd2l0aCcsJyR4LWFtei1tZXRhLWRhdGEnLCcnXSxbJ2NvbnRlbnQtbGVuZ3RoLXJhbmdlJywwLDEwNDg1NzZdXX0=');
        $('#outputForm input[name=x-amz-signature]').val('03a54db00dd429627bc66fc933e5d3b06ef67318675151c37775acbaed69b70f');
      }

      if ( $( "#codeForm" ).length )  //if element exists
      {
        $('#codeForm').attr('action', 'http://ocp.upload.yetanotherwhatever.io.s3.amazonaws.com/');
        $('#codeForm input[name=success_action_redirect]').val('http://ocp.yetanotherwhatever.io/thanks.html');
      
        $('#codeForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191011/us-east-1/s3/aws4_request');
        $('#codeForm input[name=x-amz-date]').val('20191011T000000Z');
      
        $('#codeForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMVQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOidvY3AudXBsb2FkLnlldGFub3RoZXJ3aGF0ZXZlci5pbyd9LFsnc3RhcnRzLXdpdGgnLCcka2V5JywndXBsb2Fkcy9jb2RlLyddLHsnYWNsJzoncHJpdmF0ZSd9LHsnc3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QnOidodHRwOi8vb2NwLnlldGFub3RoZXJ3aGF0ZXZlci5pby90aGFua3MuaHRtbCd9LHsneC1hbXotYWxnb3JpdGhtJzonQVdTNC1ITUFDLVNIQTI1Nid9LHsneC1hbXotY3JlZGVudGlhbCc6J0FLSUFKRUVPRk5FRVdQR0FaTTVBLzIwMTkxMDExL3VzLWVhc3QtMS9zMy9hd3M0X3JlcXVlc3QnfSx7J3gtYW16LWRhdGUnOicyMDE5MTAxMVQwMDAwMDBaJ30seyd4LWFtei1zdG9yYWdlLWNsYXNzJzonUkVEVUNFRF9SRURVTkRBTkNZJ30sWydzdGFydHMtd2l0aCcsJyR4LWFtei1tZXRhLWRhdGEnLCcnXSxbJ2NvbnRlbnQtbGVuZ3RoLXJhbmdlJywwLDEwNDg1NzZdXX0=');
        $('#codeForm input[name=x-amz-signature]').val('00da25f9db94a7ee23ed27e459444a9423e6028a5cbc94a160848bb108708e75');
      }

      if ( $( "#internRegForm" ).length ) //if element exists
      {
        $('#internRegForm').attr('action', 'http://ocp.upload.yetanotherwhatever.io.s3.amazonaws.com/');
        $('#internRegForm input[name=success_action_redirect]').val('http://ocp.yetanotherwhatever.io/registration2019complete.html');
      
        $('#internRegForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191011/us-east-1/s3/aws4_request');
        $('#internRegForm input[name=x-amz-date]').val('20191011T000000Z');
      
        $('#internRegForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMVQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOidvY3AudXBsb2FkLnlldGFub3RoZXJ3aGF0ZXZlci5pbyd9LFsnc3RhcnRzLXdpdGgnLCcka2V5JywndXBsb2Fkcy9pbnRlcm5zaGlwUmVnaXN0cmF0aW9uLyddLHsnYWNsJzoncHJpdmF0ZSd9LHsnc3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QnOidodHRwOi8vb2NwLnlldGFub3RoZXJ3aGF0ZXZlci5pby9yZWdpc3RyYXRpb24yMDE5Y29tcGxldGUuaHRtbCd9LHsneC1hbXotYWxnb3JpdGhtJzonQVdTNC1ITUFDLVNIQTI1Nid9LHsneC1hbXotY3JlZGVudGlhbCc6J0FLSUFKRUVPRk5FRVdQR0FaTTVBLzIwMTkxMDExL3VzLWVhc3QtMS9zMy9hd3M0X3JlcXVlc3QnfSx7J3gtYW16LWRhdGUnOicyMDE5MTAxMVQwMDAwMDBaJ30seyd4LWFtei1zdG9yYWdlLWNsYXNzJzonUkVEVUNFRF9SRURVTkRBTkNZJ30sWydzdGFydHMtd2l0aCcsJyR4LWFtei1tZXRhLWRhdGEnLCcnXSxbJ2NvbnRlbnQtbGVuZ3RoLXJhbmdlJywwLDEwNDg1NzZdXX0=');
        $('#internRegForm input[name=x-amz-signature]').val('86b57afc863a56dde635e268138f8e5051b7aa4841eb3edc4dc146c290c35ec3');
      }
      break;
  }
}
/************************** END AUTO-GENERATED FORM SETUP CODE **************************/

//API gateway stage name matches env name
const stage = env;

//api gateway resource urls
const restAPIBaseURL = "https://" + ocpv2RestAPIId + ".execute-api.us-east-1.amazonaws.com/" + stage;
const inviteAPIURL = restAPIBaseURL + '/invitation';
const getRessultsAPIURL = restAPIBaseURL + '/outputtestresult';
