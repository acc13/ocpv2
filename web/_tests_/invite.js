'use strict';

const Cookies = require('js-cookie');
const $ = require('jquery');
const runtime = require('./utils/runtime-env');
const dialogs = require('./utils/dialogs');
const config = require('./utils/config');

function init()
{
  if ($('#invite-form').length) //element exists on page, then we're on the correct page
  {
    if (runtime.isBrowser)
    {
      console.log("initializing invite.html");
    }

    module.exports.__private__.prefillManagerInfo();
    config.init();

    $('#invite-form').submit(function (event) {
      module.exports.__private__.handleSubmitInviteForm(event);
    });
  }
}


function prefillManagerInfo()
{
	if (Cookies.get("manager_email") != null)
	{
		$('#manager_email').val(Cookies.get('manager_email'));
		$('#remember_me').prop( "checked", Cookies.get('remember_me'));
	}
}

function optionallySaveManagerInfo()
{
  if ($('#remember_me').is(':checked'))
  {
  	Cookies.set('manager_email', $('#manager_email').val());
  	Cookies.set('remember_me', $('#remember_me').is(':checked'));
  }
  else
  {
  	module.exports.__private__.forgetManagerInfo();
  }
}

function forgetManagerInfo()
{
  	Cookies.remove("manager_email", null);
  	Cookies.remove("remember_me", null);	
}

function validateInviteForm()
{

  if (!($('#manager_email').val().includes("symantec.com")))
  {
  	dialogs.myAlert("Manager must have a symantec.com email.");
  	return false;
  }

  if(!dialogs.myConfirm("Confirm?")) {
  	return false;
  }

  return true;

}

function disableInviteForm()
{
  $('#invite-form').hide();
  $('#results').html("Generating problem and emailing candidate...");
}

function sendUserInvite(data)
{
  $.ajax({
    type: 'POST',
    url: config.consts.inviteAPIURL,
    dataType: 'text',
    contentType: 'application/json',
    data: JSON.stringify(data),
    crossDomain: true,
    success: function () {
      	inviteSucceeded();
    },
	error: function(XMLHttpRequest, textStatus, errorThrown) {
		inviteFailed();
	}
  });
}

function inviteSucceeded()
{
	$('#results').html('<span class="success">Invite succeeded.  You may wish to notify your candidate to expect an email.<br/><br/>They may need to check their junk folder, or notify <a href="mail://andrew_chang@symantec.com">andrew_chang@symantec.com</a>.</span>');
}

function inviteFailed()
{
  	$('#results').html('<span class="fail">Invite failed. Please refresh the page and try again; or notify <a href="mail://andrew_chang@symantec.com">andrew_chang@symantec.com</a></span>');

	$('#invite-form').show();

}

function handleSubmitInviteForm(event)
{
    
  if (runtime.isBrowser) {
    event.preventDefault();
  }
	
  module.exports.__private__.optionallySaveManagerInfo();

  if (!module.exports.__private__.validateInviteForm())
  {
  	return false;
  }

  module.exports.__private__.disableInviteForm();

  var data = {
    candidateFirstName: $('#first-name').val(),
    candidateLastName: $('#last-name').val(),
    candidateEmail: $('#email').val(),
    managerEmail: $('#manager_email').val()
  };

  module.exports.__private__.sendUserInvite(data);

}

module.exports = { 
  init: init,

  __private__: {  //modules requiring this module shall not use these
    handleSubmitInviteForm: handleSubmitInviteForm,
    prefillManagerInfo: prefillManagerInfo,
    optionallySaveManagerInfo: optionallySaveManagerInfo,
    forgetManagerInfo: forgetManagerInfo,
    
    validateInviteForm: validateInviteForm,
    disableInviteForm: disableInviteForm,
    sendUserInvite: sendUserInvite,
    inviteSucceeded: inviteSucceeded,
    inviteFailed: inviteFailed
  }
};