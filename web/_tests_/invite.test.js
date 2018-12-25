const $ = require('jquery');
const Cookies = require('js-cookie');
const invite = require('./invite.js');


jest
	.dontMock('fs')
	.dontMock('jquery');

const html = require('fs').readFileSync('../invite.html').toString();

beforeEach(() => {
  
	document.documentElement.innerHTML = html;  

	invite.__private__.sendUserInvite = jest.fn().mockImplementation(() => {
		//do nothing
	});

});


test("forgetManagerInfo() clears manager email and remember me cookie", () => {

  	Cookies.set('manager_email', "foo");
  	Cookies.set('remember_me', true);

  	expect(Cookies.get('manager_email')).toBe("foo");
  	//the boolean gets saved as string??
  	expect(Cookies.get('remember_me')).toBe("true");	

	invite.__private__.forgetManagerInfo();

  	expect(Cookies.get('manager_email')).toBeUndefined();
  	expect(Cookies.get('remember_me')).toBeUndefined();
});

test("optionallySaveManagerInfo() saves cookies if checkbox checked", () => {
	
	invite.__private__.forgetManagerInfo();

	$('#remember_me').prop('checked', true);
	$('#manager_email').val("bar");

	invite.__private__.optionallySaveManagerInfo();

  	expect(Cookies.get('manager_email')).toBe("bar");
  	expect(Cookies.get('remember_me')).toBe("true");
});


test("prefillManagerInfo() fills input from cookie vals if set", () => {

	expect($('#manager_email').val()).toBe("");

  	Cookies.set('manager_email', "nacho@libre.com");
  	Cookies.set('remember_me', true);

	invite.__private__.prefillManagerInfo();  	

	expect($('#manager_email').val()).toBe("nacho@libre.com");
	expect($('#remember_me').val()).toBeTruthy();

});


test("optionallySaveManagerInfo() calls forgetManagerInfo() if 'remember me' unchecked", () => {
	
  	Cookies.set('manager_email', "fakestuff");
  	Cookies.set('remember_me', true);

	$('#remember_me').prop('checked', false);

	callF1AndExpectF2ToBeCalled(invite.__private__.optionallySaveManagerInfo, "forgetManagerInfo");
});

test("init() calls prefillManagerInfo()", () => {

	callF1AndExpectF2ToBeCalled(invite.init, "prefillManagerInfo");
});


test("validateInviteForm() fails on non symantec manager email", () => {

	$('#manager_email').val("someone@somwhere.com");

	const valid = invite.__private__.validateInviteForm();

	expect(valid).toBeFalsy();
});


test("validateInviteForm() allows symantec.com manager email", () => {

	$('#manager_email').val("someone@symantec.com");

	const valid = invite.__private__.validateInviteForm();

	expect(valid).toBeTruthy();
});

//TODO: hidden state starts as true??
test.skip("disableInviteForm() hides the invite form", () => {

	expect($('#invite-form').is(':hidden')).toBeFalsy();

	invite.__private__.disableInviteForm();

	expect($('#invite-form').is(':hidden')).toBeTruthy();
});

test("handleSubmitInviteForm() saves manager info", () => {

	callF1AndExpectF2ToBeCalled(invite.__private__.handleSubmitInviteForm, "optionallySaveManagerInfo");
});

test("handleSubmitInviteForm() validates form", () => {

	callF1AndExpectF2ToBeCalled(invite.__private__.handleSubmitInviteForm, "validateInviteForm");
});

mockValidation = jest.fn().mockImplementation( () => {
	return true;
});

test("handleSubmitInviteForm() disables form", () => {

	const saveValidation = invite.__private__.validateInviteForm;
	invite.__private__.validateInviteForm = mockValidation;
	callF1AndExpectF2ToBeCalled(invite.__private__.handleSubmitInviteForm, "disableInviteForm");
	invite.validateInviteForm = saveValidation;
});

test("handleSubmitInviteForm() submits invitation request", () => {

	const saveValidation = invite.__private__.validateInviteForm;
	invite.__private__.validateInviteForm = mockValidation;
	callF1AndExpectF2ToBeCalled(invite.__private__.handleSubmitInviteForm, "sendUserInvite");
	invite.__private__.validateInviteForm = saveValidation;
});

function callF1AndExpectF2ToBeCalled(caller, callee)
{
	const saveCallee = invite.__private__[callee];
	invite.__private__[callee] = jest.fn().mockImplementation(() => {
		saveCallee();
	});

	caller();

	expect(invite.__private__[callee]).toBeCalled();	

	invite.__private__[callee] = saveCallee;
}