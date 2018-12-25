'use strict';

const runtime = require('./runtime-env');

//quiet dialogs if not running in browser
function myAlert(message)
{
  if (runtime.isBrowser) {
      return alert(message);
  }
}

//quiet dialogs if not running in browser
function myConfirm(message)
{
  if (runtime.isNode) {
    return true;
  } else {
    return confirm(message);
  }
}

module.exports = {
	myAlert: myAlert,
	myConfirm: myConfirm
};