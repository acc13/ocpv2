'use strict';

const $ = require('jquery');
const runtime = require('./runtime-env');

function init()
{
  const stage = getStageName(); 
  const ocpv2RestAPIId = getOCPv2RestAPIID(stage);
  const restAPIBaseURL = "https://" + ocpv2RestAPIId + ".execute-api.us-east-1.amazonaws.com/" + stage;
  const inviteAPIURL = restAPIBaseURL + '/invitation';
  const getRessultsAPIURL = restAPIBaseURL + '/outputtestresult';
  const listInternsAPIURL = restAPIBaseURL + '/interns'; 

  module.exports.consts = {
    stage, 
    inviteAPIURL,
    getRessultsAPIURL,
    listInternsAPIURL
  };
}

function getStageName()
{
  let stage = "test";
  if (runtime.isBrowser)
  {
    const stage = extractSubdomain(window.location.href);
  }

  return stage;
}

//returns the name of the environment we are in
function extractSubdomain(url) {
    let hostname;

    //find & remove protocol (http, ftp, etc.)
    //then all trailing paths
    if (url.indexOf("//") > -1) {
        hostname = url.split('/')[2];
    }
    else {
        hostname = url.split('/')[0];
    }

    //find & remove port number
    hostname = hostname.split(':')[0];

    //just first part of domain name
    return hostname.split('.')[0];
}

function getOCPv2RestAPIID(stage)
{
  const mapStageIds = {
    'test':   "n8p3qg8w4c",
    'stage':  "e7r30esi5h",
    'prod':   "j17gdoueah"
  };

  let ocpv2RestAPIId = mapStageIds[stage];
}

module.exports = { 
  init: init,
  __private__: {
    getStageName: getStageName,
    extractSubdomain: extractSubdomain    
  }
};