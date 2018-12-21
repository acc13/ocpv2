'use strict'

const $ = require('jquery');

test.skip("prepareS3UploadForms() initializes a simple field (upload form success redirect)", () => {
	
	document.documentElement.innerHTML = html;

  	expect($('#outputForm input[name=success_action_redirect]').val())
  		.toBe("FFFFFFFFFFFF");

	const sub = config.prepareS3UploadForms();
	
  	expect($('#outputForm input[name=success_action_redirect]').val())
  		.toBe("http://test.yetanotherwhatever.io/submitting.html");

})
