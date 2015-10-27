var https = require("https");

var options = {
	host: "127.0.0.1",
	path: "/",
	port: '3000',
	method: 'POST',
	headers: {
		// encode the secred authentication key in the header
		'auth': '45r97diIj3099KpqnzlapEIv810nZaaS0'
	}
};

// Ignores self signed certificates
process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

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

// Write the secret password in the POST data
req.write("secret~password");
req.end();