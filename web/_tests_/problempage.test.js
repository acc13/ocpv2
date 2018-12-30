'use strict';

const $ = require('jquery');

const problems = require('./problempage').__private__;

jest
	.dontMock('fs')
	.dontMock('jquery');

const pairsHtml = require('fs').readFileSync('../problems/pairs.html').toString();
const symcodeHtml = require('fs').readFileSync('../problems/symcode.html').toString();

beforeAll(() => {
  console.log = () => {};
});

beforeEach(() => {
  	document.documentElement.innerHTML = pairsHtml;

	problems.getPathName = jest.fn().mockImplementation( () => {
		return "tp/PAGEGUID.html";
	});
});


test("init() calls prepareS3UploadForms", () => {

	callF1AndExpectF2ToBeCalled(problems.init, "prepareS3UploadForms");
});

test.skip("init() sets handleSubmitCode() as the internRegForm form submit handler ", () =>{

	problems.init();

	callF1AndExpectF2ToBeCalled(() => {$('#codeForm').trigger("submit");}, 
		"handleSubmitCode");
});

test.skip("init() sets handleSubmitOutput() as the internRegForm form submit handler ", () =>{

	problems.init();

	callF1AndExpectF2ToBeCalled(() => {$('#outputForm').trigger("submit");}, 
		"handleSubmitOutput");
});

test("getProblemName() for pairs.html is 'pairs'", () =>{

	expect(problems.getProblemName()).toBe("pairs");
});

test("getProblemName() for symcode.html is 'symcode'", () =>{

  	document.documentElement.innerHTML = symcodeHtml;

	expect(problems.getProblemName()).toBe("symcode");
});

test("codeForm metadata should include inviteId and problemName", () =>{

	problems.setFormMetaData("codeForm");

	const metadata = JSON.parse($('#codeForm input[name=x-amz-meta-data]').val());

	expect(metadata.inviteId).toBe("PAGEGUID");
	expect(metadata.problemName).toBe("pairs");
});

test("codeForm metadata should include inviteId and problemName", () =>{

	problems.setFormMetaData("outputForm");

	const metadata = JSON.parse($('#outputForm input[name=x-amz-meta-data]').val());

	expect(metadata.inviteId).toBe("PAGEGUID");
	expect(metadata.problemName).toBe("pairs");
});

test("getPageGuid() is 'PAGEGUID'", () =>{

	expect(problems.getPageGuid()).toBe("PAGEGUID");
});

test("handleSubmitOutput() calls validation", () => {
	callF1AndExpectF2ToBeCalled(problems.handleSubmitOutput, "validateSubmitOutputForm");
});

test("handleSubmitOutput() sets destination key", () => {
	callF1AndExpectF2ToBeCalled(problems.handleSubmitOutput, "buildOutputS3DestinationKey");
});

test("buildOutputS3DestinationKey() is a .txt, starts with correct prefix, and contains page guid and problem name", () =>{

	problems.buildOutputS3DestinationKey();

	const s3DestKey = $('#outputForm input[name=key]').val();

	expect(s3DestKey).toMatch(/uploads\/output\/PAGEGUID\/pairs\/.*.txt/);
});

test("handleSubmitCode() calls validation", () => {
	callF1AndExpectF2ToBeCalled(problems.handleSubmitCode, "validateSubmitCodeForm");
});

test("handleSubmitCode() sets destination key", () => {
	callF1AndExpectF2ToBeCalled(problems.handleSubmitCode, "buildCodeS3DestinationKey");
});

test("buildCodeS3DestinationKey() contains the correct prefix, the page guid, and has .zip extension", () =>{

	problems.buildCodeS3DestinationKey();

	const s3DestKey = $('#codeForm input[name=key]').val();

	expect(s3DestKey).toMatch(/uploads\/code\/PAGEGUID\/.*.zip/);
});

test("validateSubmitOutputForm() fails if no file selected", () => {
	
	problems.getOutputFileName = jest.fn().mockImplementation(() => {
		return "";
	});

	const valid = problems.validateSubmitOutputForm();

	expect(valid).toBeFalsy();
});

test("validateSubmitOutputForm() succeeds if filename present", () => {
	
	problems.getOutputFileName = jest.fn().mockImplementation(() => {
		return "outputFile.txt";
	});

	const valid = problems.validateSubmitOutputForm();

	expect(valid).toBeTruthy();
});

test("validateSubmitCodeForm() fails if no file selected", () => {
	
	problems.getCodeFileName = jest.fn().mockImplementation(() => {
		return "";
	});

	const valid = problems.validateSubmitCodeForm();

	expect(valid).toBeFalsy();
});

test("validateSubmitCodeForm() fails if not .zip file", () => {
	
	problems.getCodeFileName = jest.fn().mockImplementation(() => {
		return "codeFile.txt";
	});

	const valid = problems.validateSubmitCodeForm();

	expect(valid).toBeFalsy();
});

test("validateSubmitCodeForm() succeeds if .zip file chosen present", () => {
	
	problems.getCodeFileName = jest.fn().mockImplementation(() => {
		return "codeFile.zip";
	});

	const valid = problems.validateSubmitCodeForm();

	expect(valid).toBeTruthy();
});

function callF1AndExpectF2ToBeCalled(caller, callee)
{
	const saveCallee = problems[callee];
	problems[callee] = jest.fn().mockImplementation(() => {
		saveCallee();
	});

	caller();

	expect(problems[callee]).toBeCalled();	

	problems[callee] = saveCallee;
}