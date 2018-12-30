'use strict';

const $ = require('jquery');
const uploadform = require('./s3uploadforms');
const config = require('./config');

jest
	.dontMock('fs')
	.dontMock('jquery');


beforeAll(() => {
  console.log = () => {};
  config.init();
});

test("prepareS3UploadForms() initializes a simple field (upload form success redirect)", () => {
	
	const html = require('fs').readFileSync('../register2019.html').toString();
	document.documentElement.innerHTML = html;
	
  	expect($('#internRegForm input[name=success_action_redirect]').val())
  		.toBe("FFFFFFFFFFFF");

	const sub = uploadform.prepareS3UploadForms();
	
  	expect($('#internRegForm input[name=success_action_redirect]').val())
  		.toBe("http://test.yetanotherwhatever.io/registration2019complete.html");

});
