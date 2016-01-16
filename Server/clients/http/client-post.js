// Node.js modules
var http = require("http");
var jbs_crypto = require('jbs-crypto');


// http request options for POST
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

// Make the http request with the above POST options
var req = http.request(options, function(response) {
	
	// Capture the response sent back from the server
	// with the response event emitters
	var str = '';
	response.on('data', function(chunk) {
		str += chunk;
	});
	
	response.on('end', function() {
		console.log('Encrypted ==> \n' + str);

		jbs_crypto.decrypt(str, function(err, decrypted) {
			console.log('Decrypted ==>\n' + decrypted);
		});
	});
});


// POST data to send to server, must have correct username/password
var secretMessage = {
	"Username" : "Brad",
	"Password" : "12345",
	"method" : "POST",
	"restOfContent" : {
		"name" : "Cisco",
		"username" : "Bookers",
		"password" : "passssss"
	}
};


jbs_crypto.encrypt(secretMessage, function(err, encrypted) {

	if (err) console.log(err);

	else {
		// Write the secret password in the POST data
		req.write(encrypted);
		req.end();
	}
});