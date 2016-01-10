// Node.js modules
var https = require("https");


// https request options for POST
var options = {
	host: "127.0.0.1",
	path: "/",
	port: '3000',
	method: 'POST',
	headers: {
		// encode the secret authentication key in the header
		'tier1': '45r97diIj3099KpqnzlapEIv810nZaaS0',
		'Content-Type': 'application/json'
	}
};

// Ignores self signed certificates
process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";


// Make the https request with the above POST options
var req = https.request(options, function(response) {
	
	// Capture the response sent back from the server
	// with the response event emitters
	var str = '';
	response.on('data', function(chunk) {
		str += chunk;
	});
	
	response.on('end', function() {
		console.log(str);
	});
});


// POST data to send to server, must have correct username/password
var secretMessage = {
	"Username" : "Brad",
	"Password" : "12345",
	"method" : "PUT",
	"restOfContent" : {
		"name" : "github",	// Name of account to make changes to
		"toChange" : "password", // or password,
		"old" : "pa$$wrd2",	// Needs to be username of account to change
		"new" : "newPa$$w#d" // or new password
	}
};

// Write the secret password in the POST data
req.write(JSON.stringify(secretMessage));
req.end();