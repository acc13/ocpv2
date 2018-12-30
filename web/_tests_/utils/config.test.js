'use strict';

const $ = require('jquery');
const config = require('./config.js').__private__;

jest
	.dontMock('fs')
	.dontMock('jquery');

const html = require('fs').readFileSync('../register2019.html').toString();

beforeAll(() => {
  console.log = () => {};
});

test("tests use test env", () => {
	config.init();

	expect(config.consts.stage).toBe("test");
});