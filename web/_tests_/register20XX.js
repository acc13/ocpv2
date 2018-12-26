'use strict';

const uuidv4 = require("uuid/v4");
const $ = require("jquery");

const dialogs = require('./utils/dialogs');
const s3upload = require('./utils/s3uploadforms');
const runtime = require('./utils/runtime-env');
const config = require ('./utils/config');

const formname = "internRegForm";

function init()
{
  if ($('#internRegForm').length) //element exists on page, then we're on the correct page
  {    
    console.log("initializing register20XX.html");
    
    config.init();

	module.exports.__private__.prepareS3UploadForms();

    $('#internRegForm').submit(module.exports.__private__.handleInternRegFormSubmit);
  }
}

function handleInternRegFormSubmit(event)
{
	if (!module.exports.__private__.validateForm(config.consts.stage))
	{
		return false;
	}

	if (!dialogs.myConfirm("You will have 7 days to solve your coding problem.  Ok to continue?"))
	{
		return false;
	}

	module.exports.__private__.setDestinationKey(formname);

	module.exports.__private__.setFormMetadata(formname);

	return true;
}

function validateForm(stage)
{
	const checkboxesChecked  = 
		//both checkboxes required
		$('#full_time_student').is(':checked') &&
		$('#available_2020').is(':checked');

	const eduEmail = (stage != "ocp") ||	//ignore for testing
		$('#email').val().endsWith('.edu');

	const valid = checkboxesChecked && eduEmail;
	
	if (!valid)
	{
		const message = "Please fix:\n" +
			"    -Your email must belong to a .edu domain.\n" +
			"    -You must be full-time student in Fall, 2019\n" +
			"    -You must be available to work full-time in 2020.";
		
		dialogs.myAlert(message);
	}

	return valid;
}

function setDestinationKey(formName)
{
	  //build key
	  const keyVal = "uploads/internshipRegistration/" + uuidv4() + getUploadFileNameExt();

	  //for some reason string interpolation doesn't work here?
	  //perhaps the #?
	  $(`#${formName} input[name=key]`).val(keyVal);
}

function getUploadFileNameExt()
{
	const  resumeFile = module.exports.__private__.getResumeFileName();
	const ext = resumeFile.split('.').pop();	//pop returns last element of split

	return "." + ext;
}

//for mocking
//because this input element may only be set to empty string
//else "InvalidStateError: This input element accepts a filename, which may only be programmatically set to the empty string."
function getResumeFileName()
{
	return $('#resumeFile').val();
}

//this json is saved as metadata on the file uploaded to S3
function setFormMetadata(formName)
{
  const md = {
  	"candidateFirst" : $('#first-name').val(),
    "candidateLast" : $('#last-name').val(),
    "candidateEmail" : $('#email').val(),
    "managerEmail" : "andrew_chang@symantec.com"
  };

  module.exports.__private__.mySetMeta(formName, md);
}

function mySetMeta(formName, md)
{
  s3upload.setMeta(formName, md);
}

module.exports = { 
  init: init,
  __private__: {
  	init: init,
  	prepareS3UploadForms: s3upload.prepareS3UploadForms,
  	handleInternRegFormSubmit: handleInternRegFormSubmit,	//for mocking
  	validateForm: validateForm,
  	setDestinationKey: setDestinationKey, 
  	setFormMetadata: setFormMetadata,
  	getResumeFileName: getResumeFileName,
  	getUploadFileNameExt: getUploadFileNameExt,
  	mySetMeta: mySetMeta
  }
};