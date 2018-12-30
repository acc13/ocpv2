'use strict';

const uuidv4 = require("uuid/v4");
const $ = require('jquery');

const config = require('./utils/config');
const s3upload = require('./utils/s3uploadforms');
const dialogs = require('./utils/dialogs');
const runtime = require('./utils/runtime-env');

function init()
{
  if ($("meta[name='problemName']").length)
  {
    console.log("Initializing problem page.");
    
    config.init();

    module.exports.__private__.prepareS3UploadForms();

    $('#codeForm').submit(module.exports.__private__.handleSubmitCode);
    setFormMetaData("codeForm");

    $('#outputForm').submit(module.exports.__private__.handleSubmitOutput);
    setFormMetaData("outputForm");
  }
}

function getProblemName()
{
  var problemName = $("meta[name='problemName']").attr("content");
  return problemName;
}

//Submit test output form metadata
function setFormMetaData(formName)
{
  const md = {
    "pageName" : getPageGuid(),
    "inviteId" : getPageGuid(), //
    "problemName" : getProblemName()
  };

  s3upload.setMeta(formName, md);
}

//given "http://foo.bar/index.html"
//returns "index"
//the page name is a GUID that is tied to a candidate invitation
function getPageGuid()
{
  const path = module.exports.__private__.getPathName();
  const filename = path.split("/").pop();
  return filename.split('.').slice(0, -1).join('.');
}

//for test mocking
function getPathName()
{
  return window.location.pathname;
}

//Key Generation
/* key for solution submission */
//TODO break this up into validation, and prepare key/metadata
function handleSubmitOutput()
{
  __private__.buildOutputS3DestinationKey();

  if (!__private__.validateSubmitOutputForm())
  {
    return false;
  }
}

function validateSubmitOutputForm()
{
  const fileName = __private__.getOutputFileName();
  if(!fileName || fileName.length == 0)
  {
        dialogs.myAlert("No file selected.");
        return false;
  }

  return true;
}

//this function must be mocked, because the file input cannot be set programmatically
function getOutputFileName()
{
  const fileName = $("#outputFile").val();
  return fileName;
}

function buildOutputS3DestinationKey()
{  
  //build key
  const keyVal = "uploads/output/" + getPageGuid() + "/" + getProblemName() + "/" + uuidv4() + ".txt";

  console.log("destination key: " + keyVal);

  $('#outputForm input[name=key]').val(keyVal);
}

// TODO break this function up
// int validation and prepare key/metadata
function handleSubmitCode()
{  
  __private__.buildCodeS3DestinationKey();

  if (!__private__.validateSubmitCodeForm())
  {
    return false;
  }

  return true;

}

function validateSubmitCodeForm()
{
  const fileName = __private__.getCodeFileName();

  if(!fileName || fileName.length == 0)
  {
    dialogs.myAlert("No file selected.");
    return false;
  }
  
  if (-1 === fileName.indexOf(".zip"))
  {
    dialogs.myAlert ("Selected file is not a .zip file.\nPlease review the upload directions.");
    return false;
  }

  return true;
}

//this function must be mocked, because the file input cannot be set programmatically
function getCodeFileName()
{
  const fileName = $("#codeFile").val();
  return fileName;
}

function buildCodeS3DestinationKey()
{  
  const keyVal = "uploads/code/" + getPageGuid() + "/" + uuidv4() + ".zip";

  console.log("destination key: " + keyVal);

  $('#codeForm input[name=key]').val(keyVal);
}

module.exports = {
  init: init,
  __private__: {
    init: init,
    getProblemName: getProblemName,
    getPathName: getPathName,
    getPageGuid: getPageGuid,
    handleSubmitCode: handleSubmitCode,
    handleSubmitOutput: handleSubmitOutput,
    prepareS3UploadForms: s3upload.prepareS3UploadForms,
    setFormMetaData: setFormMetaData,
    buildOutputS3DestinationKey: buildOutputS3DestinationKey,
    buildCodeS3DestinationKey: buildCodeS3DestinationKey,
    validateSubmitOutputForm: validateSubmitOutputForm,
    getOutputFileName: getOutputFileName,
    validateSubmitCodeForm: validateSubmitCodeForm,
    getCodeFileName: getCodeFileName
  }
};

const __private__ = module.exports.__private__;