module.exports.isNode = (typeof(process) !== 'undefined' && typeof(process.stdout) !== 'undefined');
module.exports.isBrowser = !module.exports.isNode;
module.exports.isLocalHtmlPage = (window.location.protocol === "file:");