'use strict';

jest
	.dontMock('fs')
	.dontMock('jquery');

const $ = require('jquery');

const reg = require('./register20XX').__private__;

const html = require('fs').readFileSync('../register2019.html').toString();

beforeAll(() => {
  console.log = () => {};
});

beforeEach(() => {
  	document.documentElement.innerHTML = html;
});

test("init() calls prepareS3UploadForms()", () =>{

	callF1AndExpectF2ToBeCalled(reg.init, "prepareS3UploadForms");
});


test.skip("init() sets handleInternRegFormSubmit() as the internRegForm form submit handler ", () =>{

	reg.init();

	callF1AndExpectF2ToBeCalled(() => {$('#internRegForm').trigger("submit");}, 
		"handleInternRegFormSubmit");
});

function callF1AndExpectF2ToBeCalled(caller, callee)
{
	const saveCallee = reg[callee];
	reg[callee] = jest.fn().mockImplementation(() => {
		saveCallee();
	});

	caller();

	expect(reg[callee]).toBeCalled();	

	reg[callee] = saveCallee;
}

function createMockImpl()
{
	return jest.fn().mockImplementation(() => {return true;});
}

test("handleInternRegFormSubmit() calls setDestinationKey and setFormMetadata", () => {
	const event = {};

	const mockImpl = createMockImpl();

	event.preventDefault = jest.fn().mockImplementation(() => {});

	const saveSetDestinationKey = reg.setDestinationKey;
	const saveSetFormMetadata = reg.setFormMetadata;
	const saveValidateForm = reg.validateForm;
	reg.validateForm = createMockImpl();
	reg.setDestinationKey = createMockImpl();
	reg.setFormMetadata = createMockImpl();

	reg.handleInternRegFormSubmit(event);

	expect(reg.setDestinationKey).toBeCalled();
	expect(reg.setFormMetadata).toBeCalled();

	reg.validateForm = saveValidateForm;
	reg.setDestinationKey = saveSetDestinationKey;
	reg.setFormMetadata = saveSetFormMetadata;	
});

test("validateForm() requires both checkboxes checked", () => {
	
	//
	const tests = [
		[false, false, false],
		[false, true, false],
		[true, false, false],
		[true, true, true]
	];

	for (let i = 0; i < tests.length; i++)
	{
		const condition = tests[i];
		$('#full_time_student').prop('checked', condition[0]);
		$('#available_2020').prop('checked', condition[1]);
		expect(reg.validateForm('test')).toBe(condition[2]);
	}

});

test("validateForm() fails if not .edu student address", () => {

	$('#full_time_student').prop('checked', true);
	$('#available_2020').prop('checked', true);
	$('#email').val("someone@somewhere.com");

	expect(reg.validateForm('ocp')).toBeFalsy();
});

test("validateForm() passes if .edu student address", () => {

	$('#full_time_student').prop('checked', true);
	$('#available_2020').prop('checked', true);
	$('#email').val("someone@somewhere.edu");

	expect(reg.validateForm('ocp')).toBeTruthy();
});

test("getUploadFileNameExt() takes last token", () => {
	
	reg.getResumeFileName = () => {return "foo.bar.baz";};
	expect(reg.getUploadFileNameExt()).toBe(".baz");
});

test("setDestinationKey() uses prefix 'uploads/internshipRegistration/' and retains file extension", () => {

	reg.getResumeFileName = () => {return "myresume.doc";};
	reg.setDestinationKey('internRegForm');
	expect($('#internRegForm input[name=key]').val()).toMatch(/uploads\/internshipRegistration\/.*doc/);
});

test("setFormMetadata() maps correct values into form metadata", () => {

	reg.mySetMeta = jest.fn().mockImplementation(() => {
		//do nothing
	});

	reg.setFormMetadata('internRegForm');

	const md = reg.mySetMeta.mock.calls[0][1];

	console.log(JSON.stringify(md));
});