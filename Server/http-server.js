// Node.js modules
var http = require('http');
var url = require('url');


/*
	Starts the http server
*/
function start(route, handle) {
	
	http.createServer(function(req, res) {		

    // Display 'server hit' message
		console.log("server hit");

    // Get pathname
		var pathname = url.parse(req.url).pathname;
		
		// Check for authentication in header
		if (req.headers.tier1 === '45r97diIj3099KpqnzlapEIv810nZaaS0') {		
			
			// Route to the desired logic based on the pathname
			route(handle, pathname, req, res);	
			
		} else {
			// If tier1 auth is incorrect
			displayErrorMessage(res);
		}

	}).listen(3000, 
		function() { console.log("Server started at http://localhost:3000"); });

	console.log("Server Started");
}


/*
	Send error message to client
*/
function displayErrorMessage(response) {
	response.writeHead(404, {"Content-Type": "text/html"});
	response.write("<h1>404 Not Found</h1>");
	response.end();
}



// Create Node.js custom module named start === 'function start()' on line 9
exports.start = start;