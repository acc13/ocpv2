
/* Global Vars & Config */
var isProd = window.location.href.indexOf("test.yetanotherwhatever.io") == -1;
var isTest = !isProd;


var inviteAPIURL = 'https://k64lqz09lc.execute-api.us-east-1.amazonaws.com/test/invitation';
if (isTest)
	inviteAPIURL = 'https://k64lqz09lc.execute-api.us-east-1.amazonaws.com/test/invitation';

