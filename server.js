var http = require('http');
var url = require('url');
var querystring = require('querystring');


function start(route, handle) {
	
	http.createServer(function(request, response) {
		
		var query = url.parse(request.url).query;
		var username = querystring.parse(query)['username'];
		var password = querystring.parse(query)['password'];
		
		if (username.toLowerCase() === 'brad' && password === 'password') {
			response.writeHead(200, {'Content-Type': 'text/plain'});
			response.end("Access Granted!!");
		} else {
			response.writeHead(400, {'Content-Type': 'text/plain'});
			response.end("Access Denied Son!!");
		}
		
	}).listen(3000, function() {
		console.log("Server started at 127.0.0.1:3000");
	});
	
}