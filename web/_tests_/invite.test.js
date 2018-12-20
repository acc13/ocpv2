const $ = require('jquery');
const Cookies = require('js-cookie');
const invite = require('./invite.js');


jest
	.dontMock('fs')
	.dontMock('jquery');

const html = require('fs').readFileSync('../invite.html').toString();

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
})

test("optionallySaveManagerInfo() saves cookies if checkbox checked", () => {
	
	invite.forgetManagerInfo();

	$('#remember_me').prop('checked', true);
	$('#manager_email').val("bar");

	invite.optionallySaveManagerInfo();

  	expect(Cookies.get('manager_email')).toBe("bar");
  	expect(Cookies.get('remember_me')).toBe("true");
})


test("optionallySaveManagerInfo() calls forgetManagerInfo() if 'remember me' unchecked", () => {
	
  	Cookies.set('manager_email', "fakestuff");
  	Cookies.set('remember_me', true);

	$('#remember_me').prop('checked', false);

	callF1AndExpectF2ToBeCalled("optionallySaveManagerInfo", "forgetManagerInfo")
})

test("initPage() calls exportsprefillManagerInfo()", () => {

	callF1AndExpectF2ToBeCalled("initPage", "prefillManagerInfo")

})


test("validateInviteForm() fails on non symantec manager email", () => {

	$('#manager_email').val("someone@somwhere.com");

	const valid = invite.validateInviteForm();

	expect(valid).toBeFalsy();
})


test("validateInviteForm() allows symantec.com manager email", () => {

	$('#manager_email').val("someone@symantec.com");

	const valid = invite.validateInviteForm();

	expect(valid).toBeTruthy();
})

//TODO: hidden state starts as true??
test.skip("disableInviteForm() hides the invite form", () => {

	expect($('#invite-form').is(':hidden')).toBeFalsy();

	invite.disableInviteForm();

	expect($('#invite-form').is(':hidden')).toBeTruthy();
})

test("handleSubmitInviteForm() saves manager info", () => {

	callF1AndExpectF2ToBeCalled("handleSubmitInviteForm", "optionallySaveManagerInfo")
})

test("handleSubmitInviteForm() validates form", () => {

	callF1AndExpectF2ToBeCalled("handleSubmitInviteForm", "validateInviteForm")
})

mockValidation = jest.fn().mockImplementation( () => {
	return true;
});

test("handleSubmitInviteForm() disables form", () => {

	const saveValidation = invite.validateInviteForm;
	invite.validateInviteForm = mockValidation;
	callF1AndExpectF2ToBeCalled("handleSubmitInviteForm", "disableInviteForm")
	invite.validateInviteForm = saveValidation;
})

test("handleSubmitInviteForm() submits invitation request", () => {

	const saveValidation = invite.validateInviteForm;
	invite.validateInviteForm = mockValidation;
	callF1AndExpectF2ToBeCalled("handleSubmitInviteForm", "sendUserInvite")
	invite.validateInviteForm = saveValidation;
})

function callF1AndExpectF2ToBeCalled(caller, callee)
{
	const saveCallee = invite[callee];
	invite[callee] = jest.fn().mockImplementation(() => {
		saveCallee();
	});

	invite[caller]();

	expect(invite[callee]).toBeCalled();	

	invite[callee] = saveCallee;
}