function start(request, response) {
	
	var mongoose = require('mongoose');
	mongoose.connect('mongodb://127.0.0.1/UserCache');
	
	var db = mongoose.connection;
	db.on('error', console.error.bind(console, 'Connection error:'));
	db.once('open', function (callback) {
		console.log('Database opened');
	});
	
	var Password = mongoose.Schema({
		name: String,
		username: String,
		password: String
	});
	
	var PWmodel = mongoose.model('Store', Password);
	
	
	
	//Capture POST data sent from client with request emitters
	var str = '';		
	request.on('data', function(chunk) {
		str += chunk;
	});
	
	request.on('end', function() {
		
		// JSON.parse(string); --> string to JSON
		// JSON.stringify(obj); --> JSON to string
		var newstr = JSON.parse(str);				
		
		// Verify correct POST data for access to server
		if (newstr.Username === 'Brad' && newstr.Password === '12345') {
			
			// Display access to the client
			response.writeHead(200, {"Content-Type": "text/plain"});
			response.write("You have access to the kingdom");
			response.end();
			
			// DO SECRET STUFF WITH CLIENT POST HERE 
			if (newstr.method === 'POST')
				Post(newstr.restOfContent, response);
			else if (newstr.method === 'GET')
				Get(newstr.restOfContent, response);
			else if (newstr.method === 'DELETE')
				Delete(newstr.restOfContent, response);
			else if (newstr.method === 'PUT')
				Update(newstr.restOfContent, response);
				
			console.log(newstr.restOfContent);
			
		} else {
			// If tier1 auth was correct but username/password wrong
			displayErrorMessage(response);
		}
	});		
}


function Post(otherContent, response) {
	console.log('in post');
	
	console.log('In post with ' + JSON.stringify(otherContent));
}

function Get(otherContent, response) {
	console.log('in get');
	
	console.log('In post with ' + JSON.stringify(otherContent));
}

function Delete(otherContent, response) {
	console.log('in delete');
	
	console.log('In post with ' + JSON.stringify(otherContent));
}

function Update(otherContent, response) {
	console.log('in put');
	
	console.log('In post with ' + JSON.stringify(otherContent));
}

function displayErrorMessage(response) {
	response.writeHead(404, {"Content-Type": "text/plain"});
	response.write("Denied boy");
	response.end();
}


exports.start = start;