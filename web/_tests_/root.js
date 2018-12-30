const $ = require('jquery');

const invite = require('./invite.js');
const registerIntern = require('./register20XX.js');
const problemPage = require('./problempage.js');

$(document).ready(function(){

	invite.init();
	registerIntern.init();
	problemPage.init();
});
