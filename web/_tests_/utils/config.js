'use strict';

const $ = require('jquery');
const runtime = require('./runtime-env');

function init()
{
  const stage = getStageName(); 
  const ocpv2RestAPIId = getOCPv2RestAPIID(stage);
  const restAPIBaseURL = `https://${ocpv2RestAPIId}.execute-api.us-east-1.amazonaws.com/${stage}`;
  const inviteAPIURL = `${restAPIBaseURL}/invitation`;
  const getRessultsAPIURL = `${restAPIBaseURL}/outputtestresult`;
  const listInternsAPIURL = `${restAPIBaseURL}/interns`; 

  module.exports.consts = {
    stage, 
    inviteAPIURL,
    getRessultsAPIURL,
    listInternsAPIURL
  };

  module.exports.__private__.consts = module.exports.consts;

  console.log("config.consts: " + JSON.stringify(module.exports.consts));
}

function getStageName()
{

  if (runtime.isNode)
  {
    console.log("Node modules use 'test' env.");
    return "test";
  }

  if (runtime.isLocalHtmlPage)
  {
    console.log("Local file testing uses 'test' env.");
    return "test";
  }
  
  let stage = "test";
  stage = window.location.hostname.split(".")[0];
  
  return stage;
}

function getOCPv2RestAPIID(stage)
{
  const mapStageIds = {
    'test':   "n8p3qg8w4c",
    'stage':  "e7r30esi5h",
    'prod':   "j17gdoueah"
  };

  return mapStageIds[stage];
}

module.exports = { 
  init: init,
  __private__: {
    init: init,
    getStageName: getStageName
  }
};