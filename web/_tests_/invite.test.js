const $ = require('jquery');
const Cookies = require('js-cookie');
const invite = require('./invite.module.js');


jest
	.dontMock('fs')
	.dontMock('jquery');

const html = require('fs').readFileSync('../invite.html').toString();


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
	document.documentElement.innerHTML = html;

	invite.forgetManagerInfo();

	$('#remember_me').prop('checked', true);
	$('#manager_email').val("bar");

	invite.optionallySaveManagerInfo();

  	expect(Cookies.get('manager_email')).toBe("bar");
  	expect(Cookies.get('remember_me')).toBe("true");
})


test("optionallySaveManagerInfo() calls forgetManagerInfo() if 'remember me' unchecked", () => {
	document.documentElement.innerHTML = html;

  	Cookies.set('manager_email', "fakestuff");
  	Cookies.set('remember_me', true);

	$('#remember_me').prop('checked', false);

	const forgetManagerInfo = invite.forgetManagerInfo;
	invite.forgetManagerInfo = jest.fn(() => {
		forgetManagerInfo();
	});

	invite.optionallySaveManagerInfo();

	expect(invite.forgetManagerInfo).toBeCalled();

	invite.forgetManagerInfo = forgetManagerInfo;
})

test("initPage() calls exportsprefillManagerInfo()", () => {

	const prefillManagerInfo = invite.prefillManagerInfo
	invite.prefillManagerInfo = jest.fn(() => {
		prefillManagerInfo();
	});
	
	invite.initPage();

	expect(invite.prefillManagerInfo).toBeCalled();

	invite.prefillManagerInfo = prefillManagerInfo;

})


test("validateInviteForm() fails on non symantec manager email", () => {

	document.documentElement.innerHTML = html;

	$('#manager_email').val("someone@somwhere.com");

	const valid = invite.validateInviteForm();

	expect(valid).toBeFalsy();
})


test("validateInviteForm() allows symantec.com manager email", () => {

	document.documentElement.innerHTML = html;

	$('#manager_email').val("someone@symantec.com");

	const valid = invite.validateInviteForm();

	expect(valid).toBeTruthy();
})

