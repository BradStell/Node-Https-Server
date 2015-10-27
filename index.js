var server = require("./secure-server");
var router = require("./route");
var requestHandlers = require("./requestHandlers");

var handle = {};
handle["/"] = requestHandlers.authenticate;

server.start(router.route, handle);