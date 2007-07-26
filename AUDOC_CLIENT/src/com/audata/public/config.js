/*
 * AuDoc 2 client config file
 */

var configItems = new Array();

// the url setting sets the url of the AuDoc server.
// Leave commented if the AUDOC_CLIENT and 
// AUDOC_SERVER folders are installed side by side.
configItems["url"] = "http://localhost/AUDOC_SERVER/";

// The network timeout in milliseconds
configItems["timeout"] = "500000";

//available locales should be entered as 1 string in the format
// <name>:<code>;<name>:<code>
configItems["locales"] = "Italian:it";"English:en";

configItems["user1"] = "binnum";