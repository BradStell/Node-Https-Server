// Require my custom Node.js modules
var server = require("./secure-server");
var router = require("./route");
var requestHandlers = require("./requestHandlers");

// Create associative array to deal with handling 
// routing paths with functions
var handle = {};
handle["/"] = requestHandlers.start;


// Start the server
server.start(router.route, handle);