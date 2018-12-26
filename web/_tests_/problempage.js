'use strict';

const uuidv4 = require("uuid/v4");
const $ = require('jquery');

const config = require('./utils/config');
const s3upload = require('./utils/s3uploadforms');

function getProblemName()
{
  //this var is set by the base page
  return problemName;
}


//Submit test output form metadata
function setCodeMeta(formName, problemName, email)
{
  const myObj = {"email" : email,
    "inviteId" : getFilenameNoExtension(),
    "problemName" : getProblemName()
  };

  s3upload.setMeta(formName, myObj);
}


function setOutputMeta(formName, inviteId)
{
  const myObj = {
    "inviteId" : inviteId,
    "problemName" : getProblemName()
  };

  s3upload.setMeta(formName, myObj);
}


//Key Generation
/* key for solution submission */
//TODO break this up into validation, and prepare key/metadata
function prepareoutputForm()
{
  s3upload.prepareS3UploadForms();

  //validate
  if(!document.getElementById("outputFile").value)
  {
        alert("No file selected.");
        return false;
  }

  //build key
  const keyVal = "uploads/output/" + getFilenameNoExtension() + "/" + getProblemName() + "/" + uuidv4() + ".txt";

  console.log("keyval" + keyVal);

  const formName = "outputForm";
  const form = document.getElementById(formName);
  let keys = form.querySelectorAll("[name=key]");
  let keyInput = keys[0];
  keyInput.value = keyVal;

  console.log(keyInput.value);

  setOutputMeta(formName, getFilenameNoExtension());
}


//given "http://foo.bar/index.html"
//returns "index"
function getFilenameNoExtension()
{
  const path = window.location.pathname;
  const filename = path.split("/").pop();
  return filename.split('.').slice(0, -1).join('.');
}


// TODO break this function up
// int validation and prepare key/metadata
function preparecodeForm()
{

  s3upload.prepareS3UploadForms();
  
  /*
   * VALIDATION 
   */
  const email = getAndValidateEmail();
  if (!email)
  {
    return false;
  }

  const fileName = document.getElementById("codeFile").value;
  if(!fileName)
  {
    alert("No file selected.");
    return false;
  }
  
  if (-1 === fileName.indexOf(".zip"))
  {
    alert ("Selected file is not a .zip file.");
    return false;
  }  

  //build key
  const keyVal = "uploads/code/" + getFilenameNoExtension() + "/" + uuidv4() + ".zip";

  console.log("keyval" + keyVal);

  const formName = "codeForm";
  const form = document.getElementById(formName);
  let keys = form.querySelectorAll("[name=key]");
  let keyInput = keys[0];
  keyInput.value = keyVal;

  console.log("keyInput.value" + keyInput.value);

  setCodeMeta(formName, getProblemName(), email);

  return true;

}




//////HANDLING THE OPTIONAL EMAIL FIELD
function isDynamicPage()
{
  //page will have "/tp/" folder in the path
  //and will be named "<UUID>.html"
  //TODO this is a brittle pattern...
  //breaks if the paths change

  const path = window.location.pathname;
  const pageName = path.split("/").pop();

  const example = "0C109B3C-1FC3-4EEB-AF02-D604D476FF74.html";
  const isDyn = path.indexOf("tp/") !== -1 && pageName.length === example.length;

  return (isDyn);
}

$(document).ready(function(){
  if (isDynamicPage())
  {
    $("#emaildiv").hide();
  }
});


function getAndValidateEmail()
{

  let email = "emailindb";
  if (!isDynamicPage())
  {
    const emailInput = document.getElementById("email");
    email = emailInput.value;

    if (!email || email.indexOf("@") == -1)
    {
      alert("Please enter a valid email address.");
      emailInput.className="fixThis";
      emailInput.focus();
      return null;
    }
  }

  return email;
}

