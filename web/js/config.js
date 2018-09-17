
/* Global Vars & Config */
var isProd = window.location.href.indexOf("test.yetanotherwhatever.io") == -1;
var isTest = !isProd;

var stage = isProd? "prod": "test";
var ocpv2RestAPIId = "wbwgw94qf2";

var restAPIBaseURL = "https://" + ocpv2RestAPIId + ".execute-api.us-east-1.amazonaws.com/" + stage;

var inviteAPIURL = restAPIBaseURL + '/invitation';

