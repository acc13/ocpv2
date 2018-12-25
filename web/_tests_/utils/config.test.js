'use strict';

const $ = require('jquery');
const config = require('./config.js');

jest
	.dontMock('fs')
	.dontMock('jquery');

const html = require('fs').readFileSync('../register2019.html').toString();

test("tests use stage env", () => {
	config.init();

	expect(config.consts.stage).toBe("test");
});

test("extractSubdomain() works properly", () => {
	const domain = "http://test.yetanotherwhatever.io/foo/bar.baz";
	const sub = config.__private__.extractSubdomain(domain);
	expect(sub).toBe("test");
});

test("extractSubdomain() works properly", () => {
	const domain = "https://stage.yetanotherwhatever.io/yo?whatever";
	const sub = config.__private__.extractSubdomain(domain);
	expect(sub).toBe("stage");
});

test("extractSubdomain() works properly", () => {
	const domain = "prod.yetanotherwhatever.io/yo/yo/yo/what?what?";
	const sub = config.__private__.extractSubdomain(domain);
	expect(sub).toBe("prod");
});