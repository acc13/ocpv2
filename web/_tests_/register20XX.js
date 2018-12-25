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
    if (runtime.isBrowser)
    {
      console.log("initializing register20XX.html");
    }

	module.exports.__private__.prepareS3UploadForms();

    $('#internRegForm').submit(function (event) {
      module.exports.__private__.handleInternRegFormSubmit(event);
    });
  }
}

function handleInternRegFormSubmit()
{
	  if (!module.exports.__private__.validateForm())
	  {
	  	return false;
	  }

	  if (!dialogs.myConfirm("You will have 7 days to solve your coding problem.  Ok to continue?"))
	  {
	  	return false;
	  }
	  
	  module.exports.__private__.setDestinationKey(formname);

	  module.exports.__private__.setFormMetadata(formname);
}

function validateForm()
{
	const checkboxesChecked  = 
		//both checkboxes required
		$('#full_time_student').is(':checked') &&
		$('#available_2020').is(':checked');

	const eduEmail = (config.stage != "ocp") ||	//ignore for testing
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

	  $('#${formName} input[name=key]').val(keyVal);
}

function getUploadFileNameExt()
{
	const  resumeFile = $('#resumeFile').val();
	const ext = resumeFile.split('.').pop();	//pop returns last element of split

	return "." + ext;
}


//this json is saved as metadata on the file uploaded to S3
function setFormMetadata(formName)
{
  const myObj = {
  	"candidateFirst" : $('#first-name').val(),
    "candidateLast" : $('#last-name').val(),
    "candidateEmail" : $('#email').val(),
    "managerEmail" : "andrew_chang@symantec.com"
  };

  s3upload.setMeta(formName, myObj);
}

module.exports = { 
  init: init,
  __private__: {
  	prepareS3UploadForms: s3upload.prepareS3UploadForms,
  	handleInternRegFormSubmit: handleInternRegFormSubmit,	//for mocking
  	validateForm: validateForm
  }
};