'use strict';

const config = require('./config.js').__private__;

jest
	.dontMock('fs')
	.dontMock('jquery');

beforeAll(() => {
  console.log = () => {};
});

test("tests use test env", () => {
	config.init();

	expect(config.consts.stage).toBe("test");
});