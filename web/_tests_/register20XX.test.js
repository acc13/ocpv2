'use strict';

jest
	.dontMock('fs')
	.dontMock('jquery');

const $ = require('jquery');

const reg = require('./register20XX');

const html = require('fs').readFileSync('../register2019.html').toString();


beforeEach(() => {
  	document.documentElement.innerHTML = html;
});

test("init() calls prepareS3UploadForms()", () =>{

	callF1AndExpectF2ToBeCalled(reg.init, "prepareS3UploadForms");
});


test("init() sets handleInternRegFormSubmit() as the internRegForm form submit handler ", () =>{


	reg.init();

	callF1AndExpectF2ToBeCalled($('#internRegForm').submit, "handleInternRegFormSubmit");

	// const savedHandler = reg.__private__.handleInternRegFormSubmit;
	// reg.__private__.handleInternRegFormSubmit = jest.fn().mockImplementation(() => {
	// 	savedHandler();
	// });


	// expect(reg.__private__.handleInternRegFormSubmit).toBeCalled();

	// reg.__private__.handleInternRegFormSubmit = savedHandler;
});


function callF1AndExpectF2ToBeCalled(caller, callee)
{
	const saveCallee = reg.__private__[callee];
	reg.__private__[callee] = jest.fn().mockImplementation(() => {
		saveCallee();
	});

	caller();

	expect(reg.__private__[callee]).toBeCalled();	

	reg.__private__[callee] = saveCallee;
}