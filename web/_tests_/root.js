const invite = require('./invite.js');
const registerIntern = require('./register20XX.js');
const $ = require('jquery');

$(document).ready(function(){

	invite.init();
	registerIntern.init();
});
