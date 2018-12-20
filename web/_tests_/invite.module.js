'use strict';

const Cookies = require('js-cookie');
const $ = require('jquery');

$(document).ready(function(){

  initPage();

  $('#invite-form').submit(function (event) {
    handleSubmitInviteForm(event);
  });

});

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
  	module.exports.forgetManagerInfo();
  }
}

function forgetManagerInfo()
{
  	Cookies.remove("manager_email", null);
  	Cookies.remove("remember_me", null);	
}

function initPage()
{
	module.exports.prefillManagerInfo();
}

function validateInviteForm()
{

  if (!($('#manager_email').val().includes("symantec.com")))
  {
  	myAlert("Manager must have a symantec.com email.");
  	return false;
  }

  if(!myConfirm("Confirm?")) {
  	return false;
  }

  return true;

}

//quiet dialogs if not running in browser
function myAlert(message)
{
  if (typeof(process) !== 'undefined' && typeof(process.stdout) !== 'undefined') {
      //to nothing
  } else {
      return alert(message);
  }
}

//quiet dialogs if not running in browser
function myConfirm(message)
{
  if (typeof(process) !== 'undefined' && typeof(process.stdout) !== 'undefined') {
    return true;
  } else {
    return confirm(message);
  }
}

function disableInviteForm()
{
  $('#invite-form').hide();
  $('#results').html("Generating problem and emailing candidate...")	
}

function sendUserInvite(data)
{
  $.ajax({
    type: 'POST',
    url: inviteAPIURL,
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
  })
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
  event.preventDefault();  
	
  optionallySaveManagerInfo();

  if (!validateInviteForm())
  {
  	return false;
  }

  disableInviteForm();

  var data = {
    candidateFirstName: $('#first-name').val(),
    candidateLastName: $('#last-name').val(),
    candidateEmail: $('#email').val(),
    managerEmail: $('#manager_email').val()
  }

  sendUserInvite(data);

}

module.exports = { 
  prefillManagerInfo: prefillManagerInfo,
  optionallySaveManagerInfo: optionallySaveManagerInfo,
  forgetManagerInfo: forgetManagerInfo,
  initPage: initPage,
  validateInviteForm: validateInviteForm,
  disableInviteForm: disableInviteForm,
  sendUserInvite: sendUserInvite,
  inviteSucceeded: inviteSucceeded,
  inviteFailed: inviteFailed,
  handleSubmitInviteForm: handleSubmitInviteForm
};