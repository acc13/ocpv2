'use strict';

/*!
Math.uuid.js (v1.4)
http://www.broofa.com
mailto:robert@broofa.com

Copyright (c) 2010 Robert Kieffer
Dual licensed under the MIT and GPL licenses.
*/

/*
 * Generate a random uuid.
 *
 * USAGE: Math.uuid(length, radix)
 *   length - the desired number of characters
 *   radix  - the number of allowable values for each character.
 *
 * EXAMPLES:
 *   // No arguments  - returns RFC4122, version 4 ID
 *   >>> Math.uuid()
 *   "92329D39-6F5C-4520-ABFC-AAB64544E172"
 *
 *   // One argument - returns ID of the specified length
 *   >>> Math.uuid(15)     // 15 character ID (default base=62)
 *   "VcydxgltxrVZSTV"
 *
 *   // Two arguments - returns ID of the specified length, and radix. (Radix must be <= 62)
 *   >>> Math.uuid(8, 2)  // 8 character ID (base=2)
 *   "01001010"
 *   >>> Math.uuid(8, 10) // 8 character ID (base=10)
 *   "47473046"
 *   >>> Math.uuid(8, 16) // 8 character ID (base=16)
 *   "098F4D35"
 */
(function() {
  // Private array of chars to use
  const LEGAL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  const CHARS = LEGAL.split("");

  Math.uuid = function (len, radix) {
    const chars = CHARS;
    let uuid = [];
      let i;
    radix = radix || chars.length;

    if (len) {
      // Compact form
      for (i = 0;i < len; i+=1) {
        uuid[i] = chars[0 | Math.random()*radix];
      }
    } else {
      // rfc4122, version 4 form
      let r;

      // rfc4122 requires these characters
      uuid[8] = uuid[13] = uuid[18] = uuid[23] = "-";
      uuid[14] = "4";

      // Fill in random data.  At i===19 set the high bits of clock sequence as
      // per rfc4122, sec. 4.1.5
      for (i = 0;i < 36;i+=1) {
        if (!uuid[i]) {
          r = 0 | Math.random()*16;
          uuid[i] = chars[(i === 19) ? (r & 0x3) | 0x8 : r];
        }
      }
    }

    return uuid.join("");
  };

  // A more performant, but slightly bulkier, RFC4122v4 solution.  We boost performance
  // by minimizing calls to random()
  Math.uuidFast = function() {
    const chars = CHARS;
    let uuid = new Array(36);
    let rnd=0;
      let r;
    for (var i = 0;i < 36;i+=1) {
      if (i===8 || i===13 ||  i===18 || i===23) {
        uuid[i] = "-";
      } else if (i===14) {
        uuid[i] = "4";
      } else {
        if (rnd <= 0x02) rnd = 0x2000000 + (Math.random()*0x1000000)|0;
        r = rnd & 0xf;
        rnd = rnd >> 4;
        uuid[i] = chars[(i === 19) ? (r & 0x3) | 0x8 : r];
      }
    }
    return uuid.join("");
  };

  // A more compact, but less performant, RFC4122v4 solution:
  Math.uuidCompact = function() {
    return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function(c) {
      const r = Math.random()*16|0, v = c === "x" ? r : (r&0x3|0x8);
      return v.toString(16);
    });
  };
})();






function getProblemName()
{
  //this var is set by the base page
  return problemName;
}

//this sets the correct form input that translates into the metadata on the file stored in S3
function setMeta(formName, myObj)
{
  debug(JSON.stringify(myObj));

  const form = document.getElementById(formName);
  const meta = form.querySelectorAll("[name=x-amz-meta-data]");
  let metaInput = meta[0];
  metaInput.value=JSON.stringify(myObj);
}


//Submit test output form metadata
function setCodeMeta(formName, problemName, email)
{
  const myObj = {"email" : email,
    "inviteId" : getFilenameNoExtension(),
    "problemName" : getProblemName()
  };

  setMeta(formName, myObj);
}


function setOutputMeta(formName, inviteId)
{
  const myObj = {
    "inviteId" : inviteId,
    "problemName" : getProblemName()
  };

  setMeta(formName, myObj);
}


//Key Generation
/* key for solution submission */
//TODO break this up into validation, and prepare key/metadata
function prepareoutputForm()
{
  prepareS3UploadForms();

  //validate
  if(!document.getElementById("outputFile").value)
  {
        alert("No file selected.");
        return false;
  }

  //build key
  const keyVal = "uploads/output/" + getFilenameNoExtension() + "/" + getProblemName() + "/" + Math.uuid() + ".txt";

  debug("keyval" + keyVal);

  const formName = "outputForm";
  const form = document.getElementById(formName);
  let keys = form.querySelectorAll("[name=key]");
  let keyInput = keys[0];
  keyInput.value = keyVal;

  debug(keyInput.value);

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

  prepareS3UploadForms();
  
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
  const keyVal = "uploads/code/" + getFilenameNoExtension() + "/" + Math.uuid() + ".zip";

  debug("keyval" + keyVal);

  const formName = "codeForm";
  const form = document.getElementById(formName);
  let keys = form.querySelectorAll("[name=key]");
  let keyInput = keys[0];
  keyInput.value = keyVal;

  debug("keyInput.value" + keyInput.value);

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

