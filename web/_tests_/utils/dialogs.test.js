'use strict';

const dialogs = require('./dialogs');

test("alert doesn't block", () => {
	dialogs.myAlert();
});

test("confirm() doesn't block, always true", () => {
	expect(dialogs.myConfirm()).toBeTruthy();
});