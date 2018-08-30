//lambda entry point
'use strict';
console.log('Loading hello world function');
 
exports.handler = function(event, context, callback) {


    var responseCode = "200";
    var respHeaders = {};
    var responseBody = {};

	// The output from a Lambda proxy integration must be 
    // of the following JSON object. The 'headers' property 
    // is for custom response headers in addition to standard 
    // ones. The 'body' property  must be a JSON string. For 
    // base64-encoded payload, you must also set the 'isBase64Encoded'
    // property to 'true'.

    var response = {
        statusCode: responseCode,
        headers: respHeaders,
        body: JSON.stringify(responseBody)
    };
    console.log("response: " + JSON.stringify(response))
    callback(null, response);
}