'use strict';

const env = require('./runtime-env');

test("isNode is true", () => {

	expect(env.isNode).toBeTruthy();

});

test("isBrowser is false", () => {

	expect(env.isBrowser).toBeFalsy();

});