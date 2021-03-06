// Require my custom Node.js modules
var server = require("./http-server");
var router = require("./router/route");
var requestHandlers = require("./router/requestHandlers");

// Create associative array to deal with handling 
// routing paths with functions
var handle = {};
handle["/"] = requestHandlers.start;


// Start the server
server.start(router.route, handle);