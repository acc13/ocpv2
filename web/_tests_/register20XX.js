'use strict';

const dialogs = require('./dialogs.js');

function init()
{
  if ($('#internRegForm').length) //element exists on page, then we're on the correct page
  {
    if (runtime.isBrowser)
    {
      console.log("initializing register20XX.html");
    }

	prepareS3UploadForms();

    $('#internRegForm').submit(function (event) {
      prepareinternRegForm(event);
    });
  }
}

function validateForm()
{
	const checkboxesChecked  = 
		//both checkboxes required
		$('#full_time_student').is(':checked') &&
		$('#available_2020').is(':checked');

	const eduEmail = (env != "ocp") ||	//ignore for testing
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

function prepareinternRegForm()
{
	  if (!validateForm())
	  {
	  	return false;
	  }

	  if (!dialogs.myConfirm("You will have 7 days to solve your coding problem.  Ok to continue?"))
	  {
	  	return false;
	  }

	  const formName = "internRegForm";
	  
	  setDestinationKey(formName);

	  setFormMetadata(formName);
}

function setDestinationKey(formName)
{	
	  //build key
	  const keyVal = "uploads/internshipRegistration/" + Math.uuid() + getUploadFileNameExt();

	  debug("keyval: " + keyVal);

	  const form = document.getElementById(formName);
	  let keys = form.querySelectorAll("[name=key]");
	  let keyInput = keys[0];
	  keyInput.value = keyVal;

	  debug("keyInput.value: " + keyInput.value);
}

function getUploadFileNameExt()
{
	const  resumeFile = $('#resumeFile').val();
	const ext = resumeFile.split('.').pop();	//pop returns last element of split

	debug("ext: " + ext);

	return "." + ext;
}


//this json is saved as metadata on the file uploaded to S3
function setFormMetadata(formName)
{
  const myObj = {"candidateFirst" : $('#first-name').val(),
    "candidateLast" : $('#last-name').val(),
    "candidateEmail" : $('#email').val(),
    "managerEmail" : "andrew_chang@symantec.com"
  };

  setMeta(formName, myObj);
}

module.exports = { 
  init: init
}