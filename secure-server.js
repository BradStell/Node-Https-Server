// Node.js modules
var https = require('https');
var fs = require('fs');
var url = require('url');

// Read ssl certs from disc
var options = {
  key: fs.readFileSync('key.pem'),
  cert: fs.readFileSync('key-cert.pem')
};


/*
	Starts the https server
*/
function start(route, handle) {
	
	https.createServer(options, function (req, res) {
		
		console.log("server hit");
		var pathname = url.parse(req.url).pathname;
		
		// Check for authentication in header
		if (req.headers.tier1 === '45r97diIj3099KpqnzlapEIv810nZaaS0' && req.method === 'POST') {		
			
			// Capture POST data sent from client with request emitters
			var str = '';		
			req.on('data', function(chunk) {
				str += chunk;
			});
			
			req.on('end', function() {
				
				// JSON.parse(string); --> string to JSON
				// JSON.stringify(obj); --> JSON to string
				var newstr = JSON.parse(str);				
				
				// Verify correct POST data for access to server
				if (newstr.Username === 'Brad' && newstr.Password === '12345') {
					
					// Display access to the client
					res.writeHead(200, {"Content-Type": "text/plain"});
					res.write("You have access to the kindom");
					res.end();
					
					// DO SECRET STUFF WITH CLIENT HERE
					route(handle, pathname, newstr);
					//console.log(newstr.restOfContent);
					
				} else {
					// If tier1 auth was correct but username/password wrong
					displayErrorMessage(res);
				}
			});	
			
		} else {
			// If tier1 auth is incorrect
			displayErrorMessage(res);
		}
		
	}).listen(3000, function() {
						console.log("Server started at https://localhost:3000");
					});
	console.log("Server Started");
}


/*
	Send error message to client
*/
function displayErrorMessage(response) {
	response.writeHead(401, {"Content-Type": "text/plain"});
	response.write("Denied boy");
	response.end();
}



// Create Node.js custom module named start === 'function start()' on line 9
exports.start = start;