'use strict';

const $ = require('jquery');
const Cookies = require('js-cookie');
const invite = require('./invite.js').__private__;


jest
	.dontMock('fs')
	.dontMock('jquery');

const html = require('fs').readFileSync('../invite.html').toString();

beforeAll(() => {
  console.log = () => {};
});

beforeEach(() => {
  
	document.documentElement.innerHTML = html;  

	invite.sendUserInvite = jest.fn().mockImplementation(() => {
		//do nothing
	});

});


test("forgetManagerInfo() clears manager email and remember me cookie", () => {

  	Cookies.set('manager_email', "foo");
  	Cookies.set('remember_me', true);

  	expect(Cookies.get('manager_email')).toBe("foo");
  	//the boolean gets saved as string??
  	expect(Cookies.get('remember_me')).toBe("true");	

	invite.forgetManagerInfo();

  	expect(Cookies.get('manager_email')).toBeUndefined();
  	expect(Cookies.get('remember_me')).toBeUndefined();
});

test("optionallySaveManagerInfo() saves cookies if checkbox checked", () => {
	
	invite.forgetManagerInfo();

	$('#remember_me').prop('checked', true);
	$('#manager_email').val("bar");

	invite.optionallySaveManagerInfo();

  	expect(Cookies.get('manager_email')).toBe("bar");
  	expect(Cookies.get('remember_me')).toBe("true");
});


test("prefillManagerInfo() fills input from cookie vals if set", () => {

	expect($('#manager_email').val()).toBe("");

  	Cookies.set('manager_email', "nacho@libre.com");
  	Cookies.set('remember_me', true);

	invite.prefillManagerInfo();  	

	expect($('#manager_email').val()).toBe("nacho@libre.com");
	expect($('#remember_me').val()).toBeTruthy();

});


test("optionallySaveManagerInfo() calls forgetManagerInfo() if 'remember me' unchecked", () => {
	
  	Cookies.set('manager_email', "fakestuff");
  	Cookies.set('remember_me', true);

	$('#remember_me').prop('checked', false);

	callF1AndExpectF2ToBeCalled(invite.optionallySaveManagerInfo, "forgetManagerInfo");
});

test("init() calls prefillManagerInfo()", () => {

	callF1AndExpectF2ToBeCalled(invite.init, "prefillManagerInfo");
});


test("validateInviteForm() fails on non symantec manager email", () => {

	$('#manager_email').val("someone@somwhere.com");

	const valid = invite.validateInviteForm();

	expect(valid).toBeFalsy();
});


test("validateInviteForm() allows symantec.com manager email", () => {

	$('#manager_email').val("someone@symantec.com");

	const valid = invite.validateInviteForm();

	expect(valid).toBeTruthy();
});

//TODO: hidden state starts as true??
test.skip("disableInviteForm() hides the invite form", () => {

	expect($('#invite-form').is(':hidden')).toBeFalsy();

	invite.disableInviteForm();

	expect($('#invite-form').is(':hidden')).toBeTruthy();
});

test("handleSubmitInviteForm() saves manager info", () => {

	callF1AndExpectF2ToBeCalled(invite.handleSubmitInviteForm, "optionallySaveManagerInfo");
});

test("handleSubmitInviteForm() validates form", () => {

	callF1AndExpectF2ToBeCalled(invite.handleSubmitInviteForm, "validateInviteForm");
});

const mockValidation = jest.fn().mockImplementation( () => {
	return true;
});

test("handleSubmitInviteForm() disables form", () => {

	const saveValidation = invite.validateInviteForm;
	invite.validateInviteForm = mockValidation;
	callF1AndExpectF2ToBeCalled(invite.handleSubmitInviteForm, "disableInviteForm");
	invite.validateInviteForm = saveValidation;
});

test("handleSubmitInviteForm() submits invitation request", () => {

	const saveValidation = invite.validateInviteForm;
	invite.validateInviteForm = mockValidation;
	callF1AndExpectF2ToBeCalled(invite.handleSubmitInviteForm, "sendUserInvite");
	invite.validateInviteForm = saveValidation;
});

function callF1AndExpectF2ToBeCalled(caller, callee)
{
	const saveCallee = invite[callee];
	invite[callee] = jest.fn().mockImplementation(() => {
		saveCallee();
	});

	caller();

	expect(invite[callee]).toBeCalled();	

	invite[callee] = saveCallee;
}