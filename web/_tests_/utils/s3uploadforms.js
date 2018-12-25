'use strict';

const $ = require("jquery");
const config = require("./config");
config.init();

/************************** AUTO-GENERATED FORM SETUP JS **************************/
function prepareS3UploadForms() {
  switch(config.consts.stage) {
    case 'test':

      if ( $( "#outputForm" ).length )  //if element exists
      {
        $('#outputForm').attr('action', 'https://test-upload-yetanotherwhatever.s3.amazonaws.com/');
        $('#outputForm input[name=success_action_redirect]').val('http://test.yetanotherwhatever.io/submitting.html');
      
        $('#outputForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191012/us-east-1/s3/aws4_request');
        $('#outputForm input[name=x-amz-date]').val('20191012T000000Z');
      
        $('#outputForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMlQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOid0ZXN0LXVwbG9hZC15ZXRhbm90aGVyd2hhdGV2ZXInfSxbJ3N0YXJ0cy13aXRoJywnJGtleScsJ3VwbG9hZHMvb3V0cHV0LyddLHsnYWNsJzoncHJpdmF0ZSd9LHsnc3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QnOidodHRwOi8vdGVzdC55ZXRhbm90aGVyd2hhdGV2ZXIuaW8vc3VibWl0dGluZy5odG1sJ30seyd4LWFtei1hbGdvcml0aG0nOidBV1M0LUhNQUMtU0hBMjU2J30seyd4LWFtei1jcmVkZW50aWFsJzonQUtJQUpFRU9GTkVFV1BHQVpNNUEvMjAxOTEwMTIvdXMtZWFzdC0xL3MzL2F3czRfcmVxdWVzdCd9LHsneC1hbXotZGF0ZSc6JzIwMTkxMDEyVDAwMDAwMFonfSx7J3gtYW16LXN0b3JhZ2UtY2xhc3MnOidSRURVQ0VEX1JFRFVOREFOQ1knfSxbJ3N0YXJ0cy13aXRoJywnJHgtYW16LW1ldGEtZGF0YScsJyddLFsnY29udGVudC1sZW5ndGgtcmFuZ2UnLDAsMTA0ODU3Nl1dfQ==');
        $('#outputForm input[name=x-amz-signature]').val('e0fb43f216bd87d3af971ae6cfa9d71fde7b803e5df04600a50d7da8034eaa02');
      }

      if ( $( "#codeForm" ).length )  //if element exists
      {
        $('#codeForm').attr('action', 'https://test-upload-yetanotherwhatever.s3.amazonaws.com/');
        $('#codeForm input[name=success_action_redirect]').val('http://test.yetanotherwhatever.io/thanks.html');
      
        $('#codeForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191012/us-east-1/s3/aws4_request');
        $('#codeForm input[name=x-amz-date]').val('20191012T000000Z');
      
        $('#codeForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMlQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOid0ZXN0LXVwbG9hZC15ZXRhbm90aGVyd2hhdGV2ZXInfSxbJ3N0YXJ0cy13aXRoJywnJGtleScsJ3VwbG9hZHMvY29kZS8nXSx7J2FjbCc6J3ByaXZhdGUnfSx7J3N1Y2Nlc3NfYWN0aW9uX3JlZGlyZWN0JzonaHR0cDovL3Rlc3QueWV0YW5vdGhlcndoYXRldmVyLmlvL3RoYW5rcy5odG1sJ30seyd4LWFtei1hbGdvcml0aG0nOidBV1M0LUhNQUMtU0hBMjU2J30seyd4LWFtei1jcmVkZW50aWFsJzonQUtJQUpFRU9GTkVFV1BHQVpNNUEvMjAxOTEwMTIvdXMtZWFzdC0xL3MzL2F3czRfcmVxdWVzdCd9LHsneC1hbXotZGF0ZSc6JzIwMTkxMDEyVDAwMDAwMFonfSx7J3gtYW16LXN0b3JhZ2UtY2xhc3MnOidSRURVQ0VEX1JFRFVOREFOQ1knfSxbJ3N0YXJ0cy13aXRoJywnJHgtYW16LW1ldGEtZGF0YScsJyddLFsnY29udGVudC1sZW5ndGgtcmFuZ2UnLDAsMTA0ODU3Nl1dfQ==');
        $('#codeForm input[name=x-amz-signature]').val('4e56c96ee66644412a6964ec913e1ca7387069106d12ed469cd81284f19bc2e8');
      }

      if ( $( "#internRegForm" ).length ) //if element exists
      {
        $('#internRegForm').attr('action', 'https://test-upload-yetanotherwhatever.s3.amazonaws.com/');
        $('#internRegForm input[name=success_action_redirect]').val('http://test.yetanotherwhatever.io/registration2019complete.html');
      
        $('#internRegForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191012/us-east-1/s3/aws4_request');
        $('#internRegForm input[name=x-amz-date]').val('20191012T000000Z');
      
        $('#internRegForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMlQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOid0ZXN0LXVwbG9hZC15ZXRhbm90aGVyd2hhdGV2ZXInfSxbJ3N0YXJ0cy13aXRoJywnJGtleScsJ3VwbG9hZHMvaW50ZXJuc2hpcFJlZ2lzdHJhdGlvbi8nXSx7J2FjbCc6J3ByaXZhdGUnfSx7J3N1Y2Nlc3NfYWN0aW9uX3JlZGlyZWN0JzonaHR0cDovL3Rlc3QueWV0YW5vdGhlcndoYXRldmVyLmlvL3JlZ2lzdHJhdGlvbjIwMTljb21wbGV0ZS5odG1sJ30seyd4LWFtei1hbGdvcml0aG0nOidBV1M0LUhNQUMtU0hBMjU2J30seyd4LWFtei1jcmVkZW50aWFsJzonQUtJQUpFRU9GTkVFV1BHQVpNNUEvMjAxOTEwMTIvdXMtZWFzdC0xL3MzL2F3czRfcmVxdWVzdCd9LHsneC1hbXotZGF0ZSc6JzIwMTkxMDEyVDAwMDAwMFonfSx7J3gtYW16LXN0b3JhZ2UtY2xhc3MnOidSRURVQ0VEX1JFRFVOREFOQ1knfSxbJ3N0YXJ0cy13aXRoJywnJHgtYW16LW1ldGEtZGF0YScsJyddLFsnY29udGVudC1sZW5ndGgtcmFuZ2UnLDAsMTA0ODU3Nl1dfQ==');
        $('#internRegForm input[name=x-amz-signature]').val('146be80a1da196b275971357aca0960f3b08a41c2af119af7e716994f3c3814d');
      }
      break;
    case 'stage':

      if ( $( "#outputForm" ).length )  //if element exists
      {
        $('#outputForm').attr('action', 'https://stage-upload-yetanotherwhatever.s3.amazonaws.com/');
        $('#outputForm input[name=success_action_redirect]').val('http://stage.yetanotherwhatever.io/submitting.html');
      
        $('#outputForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191012/us-east-1/s3/aws4_request');
        $('#outputForm input[name=x-amz-date]').val('20191012T000000Z');
      
        $('#outputForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMlQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOidzdGFnZS11cGxvYWQteWV0YW5vdGhlcndoYXRldmVyJ30sWydzdGFydHMtd2l0aCcsJyRrZXknLCd1cGxvYWRzL291dHB1dC8nXSx7J2FjbCc6J3ByaXZhdGUnfSx7J3N1Y2Nlc3NfYWN0aW9uX3JlZGlyZWN0JzonaHR0cDovL3N0YWdlLnlldGFub3RoZXJ3aGF0ZXZlci5pby9zdWJtaXR0aW5nLmh0bWwnfSx7J3gtYW16LWFsZ29yaXRobSc6J0FXUzQtSE1BQy1TSEEyNTYnfSx7J3gtYW16LWNyZWRlbnRpYWwnOidBS0lBSkVFT0ZORUVXUEdBWk01QS8yMDE5MTAxMi91cy1lYXN0LTEvczMvYXdzNF9yZXF1ZXN0J30seyd4LWFtei1kYXRlJzonMjAxOTEwMTJUMDAwMDAwWid9LHsneC1hbXotc3RvcmFnZS1jbGFzcyc6J1JFRFVDRURfUkVEVU5EQU5DWSd9LFsnc3RhcnRzLXdpdGgnLCckeC1hbXotbWV0YS1kYXRhJywnJ10sWydjb250ZW50LWxlbmd0aC1yYW5nZScsMCwxMDQ4NTc2XV19');
        $('#outputForm input[name=x-amz-signature]').val('7d4a685935b0bc36d322f41bcea6296e8bfb2b8e78ccc74f97d1ca1a4076d69f');
      }

      if ( $( "#codeForm" ).length )  //if element exists
      {
        $('#codeForm').attr('action', 'https://stage-upload-yetanotherwhatever.s3.amazonaws.com/');
        $('#codeForm input[name=success_action_redirect]').val('http://stage.yetanotherwhatever.io/thanks.html');
      
        $('#codeForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191012/us-east-1/s3/aws4_request');
        $('#codeForm input[name=x-amz-date]').val('20191012T000000Z');
      
        $('#codeForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMlQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOidzdGFnZS11cGxvYWQteWV0YW5vdGhlcndoYXRldmVyJ30sWydzdGFydHMtd2l0aCcsJyRrZXknLCd1cGxvYWRzL2NvZGUvJ10seydhY2wnOidwcml2YXRlJ30seydzdWNjZXNzX2FjdGlvbl9yZWRpcmVjdCc6J2h0dHA6Ly9zdGFnZS55ZXRhbm90aGVyd2hhdGV2ZXIuaW8vdGhhbmtzLmh0bWwnfSx7J3gtYW16LWFsZ29yaXRobSc6J0FXUzQtSE1BQy1TSEEyNTYnfSx7J3gtYW16LWNyZWRlbnRpYWwnOidBS0lBSkVFT0ZORUVXUEdBWk01QS8yMDE5MTAxMi91cy1lYXN0LTEvczMvYXdzNF9yZXF1ZXN0J30seyd4LWFtei1kYXRlJzonMjAxOTEwMTJUMDAwMDAwWid9LHsneC1hbXotc3RvcmFnZS1jbGFzcyc6J1JFRFVDRURfUkVEVU5EQU5DWSd9LFsnc3RhcnRzLXdpdGgnLCckeC1hbXotbWV0YS1kYXRhJywnJ10sWydjb250ZW50LWxlbmd0aC1yYW5nZScsMCwxMDQ4NTc2XV19');
        $('#codeForm input[name=x-amz-signature]').val('0621c11a32ebd75d9c4a1ea562bc7d7dc8f35cdabf234b0374c2e50b1d70b560');
      }

      if ( $( "#internRegForm" ).length ) //if element exists
      {
        $('#internRegForm').attr('action', 'https://stage-upload-yetanotherwhatever.s3.amazonaws.com/');
        $('#internRegForm input[name=success_action_redirect]').val('http://stage.yetanotherwhatever.io/registration2019complete.html');
      
        $('#internRegForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191012/us-east-1/s3/aws4_request');
        $('#internRegForm input[name=x-amz-date]').val('20191012T000000Z');
      
        $('#internRegForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMlQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOidzdGFnZS11cGxvYWQteWV0YW5vdGhlcndoYXRldmVyJ30sWydzdGFydHMtd2l0aCcsJyRrZXknLCd1cGxvYWRzL2ludGVybnNoaXBSZWdpc3RyYXRpb24vJ10seydhY2wnOidwcml2YXRlJ30seydzdWNjZXNzX2FjdGlvbl9yZWRpcmVjdCc6J2h0dHA6Ly9zdGFnZS55ZXRhbm90aGVyd2hhdGV2ZXIuaW8vcmVnaXN0cmF0aW9uMjAxOWNvbXBsZXRlLmh0bWwnfSx7J3gtYW16LWFsZ29yaXRobSc6J0FXUzQtSE1BQy1TSEEyNTYnfSx7J3gtYW16LWNyZWRlbnRpYWwnOidBS0lBSkVFT0ZORUVXUEdBWk01QS8yMDE5MTAxMi91cy1lYXN0LTEvczMvYXdzNF9yZXF1ZXN0J30seyd4LWFtei1kYXRlJzonMjAxOTEwMTJUMDAwMDAwWid9LHsneC1hbXotc3RvcmFnZS1jbGFzcyc6J1JFRFVDRURfUkVEVU5EQU5DWSd9LFsnc3RhcnRzLXdpdGgnLCckeC1hbXotbWV0YS1kYXRhJywnJ10sWydjb250ZW50LWxlbmd0aC1yYW5nZScsMCwxMDQ4NTc2XV19');
        $('#internRegForm input[name=x-amz-signature]').val('6f404c214e8d8d0c8a6e04198be1ba6eaeca73a6b837bae052261832893fc8b3');
      }
      break;
    case 'ocp':

      if ( $( "#outputForm" ).length )  //if element exists
      {
        $('#outputForm').attr('action', 'https://ocp-upload-yetanotherwhatever.s3.amazonaws.com/');
        $('#outputForm input[name=success_action_redirect]').val('http://ocp.yetanotherwhatever.io/submitting.html');
      
        $('#outputForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191012/us-east-1/s3/aws4_request');
        $('#outputForm input[name=x-amz-date]').val('20191012T000000Z');
      
        $('#outputForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMlQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOidvY3AtdXBsb2FkLXlldGFub3RoZXJ3aGF0ZXZlcid9LFsnc3RhcnRzLXdpdGgnLCcka2V5JywndXBsb2Fkcy9vdXRwdXQvJ10seydhY2wnOidwcml2YXRlJ30seydzdWNjZXNzX2FjdGlvbl9yZWRpcmVjdCc6J2h0dHA6Ly9vY3AueWV0YW5vdGhlcndoYXRldmVyLmlvL3N1Ym1pdHRpbmcuaHRtbCd9LHsneC1hbXotYWxnb3JpdGhtJzonQVdTNC1ITUFDLVNIQTI1Nid9LHsneC1hbXotY3JlZGVudGlhbCc6J0FLSUFKRUVPRk5FRVdQR0FaTTVBLzIwMTkxMDEyL3VzLWVhc3QtMS9zMy9hd3M0X3JlcXVlc3QnfSx7J3gtYW16LWRhdGUnOicyMDE5MTAxMlQwMDAwMDBaJ30seyd4LWFtei1zdG9yYWdlLWNsYXNzJzonUkVEVUNFRF9SRURVTkRBTkNZJ30sWydzdGFydHMtd2l0aCcsJyR4LWFtei1tZXRhLWRhdGEnLCcnXSxbJ2NvbnRlbnQtbGVuZ3RoLXJhbmdlJywwLDEwNDg1NzZdXX0=');
        $('#outputForm input[name=x-amz-signature]').val('a6967a3e38bfa345c66747ab480d61be96c8a7d9977dba0d31fb195db4f93711');
      }

      if ( $( "#codeForm" ).length )  //if element exists
      {
        $('#codeForm').attr('action', 'https://ocp-upload-yetanotherwhatever.s3.amazonaws.com/');
        $('#codeForm input[name=success_action_redirect]').val('http://ocp.yetanotherwhatever.io/thanks.html');
      
        $('#codeForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191012/us-east-1/s3/aws4_request');
        $('#codeForm input[name=x-amz-date]').val('20191012T000000Z');
      
        $('#codeForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMlQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOidvY3AtdXBsb2FkLXlldGFub3RoZXJ3aGF0ZXZlcid9LFsnc3RhcnRzLXdpdGgnLCcka2V5JywndXBsb2Fkcy9jb2RlLyddLHsnYWNsJzoncHJpdmF0ZSd9LHsnc3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QnOidodHRwOi8vb2NwLnlldGFub3RoZXJ3aGF0ZXZlci5pby90aGFua3MuaHRtbCd9LHsneC1hbXotYWxnb3JpdGhtJzonQVdTNC1ITUFDLVNIQTI1Nid9LHsneC1hbXotY3JlZGVudGlhbCc6J0FLSUFKRUVPRk5FRVdQR0FaTTVBLzIwMTkxMDEyL3VzLWVhc3QtMS9zMy9hd3M0X3JlcXVlc3QnfSx7J3gtYW16LWRhdGUnOicyMDE5MTAxMlQwMDAwMDBaJ30seyd4LWFtei1zdG9yYWdlLWNsYXNzJzonUkVEVUNFRF9SRURVTkRBTkNZJ30sWydzdGFydHMtd2l0aCcsJyR4LWFtei1tZXRhLWRhdGEnLCcnXSxbJ2NvbnRlbnQtbGVuZ3RoLXJhbmdlJywwLDEwNDg1NzZdXX0=');
        $('#codeForm input[name=x-amz-signature]').val('543fe021ef2929945bb3ef9128ade95e5e34e5c3d6bd7a6fdc48a9bf1809b0a0');
      }

      if ( $( "#internRegForm" ).length ) //if element exists
      {
        $('#internRegForm').attr('action', 'https://ocp-upload-yetanotherwhatever.s3.amazonaws.com/');
        $('#internRegForm input[name=success_action_redirect]').val('http://ocp.yetanotherwhatever.io/registration2019complete.html');
      
        $('#internRegForm input[name=x-amz-credential]').val('AKIAJEEOFNEEWPGAZM5A/20191012/us-east-1/s3/aws4_request');
        $('#internRegForm input[name=x-amz-date]').val('20191012T000000Z');
      
        $('#internRegForm input[name=policy]').val('eydleHBpcmF0aW9uJzonMjAxOS0xMC0xMlQwMDowMDowMFonLCdjb25kaXRpb25zJzpbeydidWNrZXQnOidvY3AtdXBsb2FkLXlldGFub3RoZXJ3aGF0ZXZlcid9LFsnc3RhcnRzLXdpdGgnLCcka2V5JywndXBsb2Fkcy9pbnRlcm5zaGlwUmVnaXN0cmF0aW9uLyddLHsnYWNsJzoncHJpdmF0ZSd9LHsnc3VjY2Vzc19hY3Rpb25fcmVkaXJlY3QnOidodHRwOi8vb2NwLnlldGFub3RoZXJ3aGF0ZXZlci5pby9yZWdpc3RyYXRpb24yMDE5Y29tcGxldGUuaHRtbCd9LHsneC1hbXotYWxnb3JpdGhtJzonQVdTNC1ITUFDLVNIQTI1Nid9LHsneC1hbXotY3JlZGVudGlhbCc6J0FLSUFKRUVPRk5FRVdQR0FaTTVBLzIwMTkxMDEyL3VzLWVhc3QtMS9zMy9hd3M0X3JlcXVlc3QnfSx7J3gtYW16LWRhdGUnOicyMDE5MTAxMlQwMDAwMDBaJ30seyd4LWFtei1zdG9yYWdlLWNsYXNzJzonUkVEVUNFRF9SRURVTkRBTkNZJ30sWydzdGFydHMtd2l0aCcsJyR4LWFtei1tZXRhLWRhdGEnLCcnXSxbJ2NvbnRlbnQtbGVuZ3RoLXJhbmdlJywwLDEwNDg1NzZdXX0=');
        $('#internRegForm input[name=x-amz-signature]').val('f71f4bd809cf9e45ce72702775a283781d83a3a6637f392c154cb1f2bb194ada');
      }
      break;
  }
}
/************************** END AUTO-GENERATED FORM SETUP CODE **************************/



//this sets the correct form input that translates into the metadata on the file stored in S3
function setMeta(formName, myObj)
{
  console.log(JSON.stringify(myObj));

  const form = document.getElementById(formName);
  const meta = form.querySelectorAll("[name=x-amz-meta-data]");
  let metaInput = meta[0];
  metaInput.value=JSON.stringify(myObj);
}

module.exports = {
  prepareS3UploadForms: prepareS3UploadForms,
  setMeta: setMeta
};