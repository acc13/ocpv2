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

test("tests use stage env", () => {
	config.init();

	expect(config.consts.stage).toBe("test");
});

test("extractSubdomain() works properly", () => {
	const domain = "http://test.yetanotherwhatever.io/foo/bar.baz";
	const sub = config.extractSubdomain(domain);
	expect(sub).toBe("test");
});

test("extractSubdomain() works properly", () => {
	const domain = "https://stage.yetanotherwhatever.io/yo?whatever";
	const sub = config.extractSubdomain(domain);
	expect(sub).toBe("stage");
});

test("extractSubdomain() works properly", () => {
	const domain = "prod.yetanotherwhatever.io/yo/yo/yo/what?what?";
	const sub = config.extractSubdomain(domain);
	expect(sub).toBe("prod");
});