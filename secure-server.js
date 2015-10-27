var https = require('https');
var fs = require('fs');

var options = {
  key: fs.readFileSync('key.pem'),
  cert: fs.readFileSync('key-cert.pem')
};

function start() {
	https.createServer(options, function (req, res) {
		
		console.log("server hit");
		
		// Check for authentication in header
		if (req.headers.auth === '45r97diIj3099KpqnzlapEIv810nZaaS0' && req.method === 'POST') {		
			
			// Capture POST data sent from client with request emitters
			var str = '';		
			req.on('data', function(chunk) {
				str += chunk;
			});
			
			req.on('end', function() {
				
				// Verify correct POST data for access to server
				if (str === "secret~password") {
					
					// Display access to the client
					res.writeHead(200, {"Content-Type": "text/plain"});
					res.write("You have access to the kindom");
					res.end();
					
				} else {
					displayErrorMessage(res);
				}
			});	
			
		} else {
			displayErrorMessage(res);
		}
		
	}).listen(3000, function() {console.log("Server started at https://localhost:3000");});
}

console.log("Server Started");

function displayErrorMessage(response) {
	response.writeHead(401, {"Content-Type": "text/plain"});
	response.write("Denied boy");
	response.end();
}

exports.start = start;