var Store;
var mongoose;
Store = require('./store-schema'); 
mongoose = require('mongoose');


function start(request, response) {	
	
	mongoose.connect('mongodb://127.0.0.1/UserCache');
	
	// Make sure db connection is good
	var db = mongoose.connection;
	db.on('error', console.error.bind(console, 'Connection error:'));	
	db.once('open', function (callback) {
		console.log('Database opened');
	});
	
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
			
			// DO SECRET STUFF WITH CLIENT POST HERE 
			if (newstr.method === 'POST')
				Post(newstr.restOfContent, response);
			else if (newstr.method === 'GET')
				Get(newstr.restOfContent, response);
			else if (newstr.method === 'DELETE')
				Delete(newstr.restOfContent, response);
			else if (newstr.method === 'PUT')
				Update(newstr.restOfContent, response);
			
		} else {
			// If tier1 auth was correct but username/password wrong
			displayErrorMessage(response);
		}
	});		
}


function Post(otherContent, response) {
	console.log('In POST');
	response.writeHead(200, {'Content-Type': 'text/plan'});
	
	// Query the db to see if the item exists already
	Store.findOne({ 'name': (otherContent.name).toLowerCase() }, function (err, pass) {
		
		if (err) console.log('ERROR:' + err);
		
		// If a document exists in db with name, check to see if username
		// already exists in that document
		if (pass) {
						
			var exists = false;			
			for (var i = 0; i < pass.accounts.length; i++) {
				if (pass.accounts[i].username === otherContent.username)
					exists = true;
			}
			
			if (!exists) {
				addToExistingPass(pass, otherContent, response);
			} else {
				console.log('Account Already Exists');
				response.write('\nUsername Already Exists');
				response.end();
			}
		}		 
		
		// If the password does not already exist add it to the db
		else {			
			saveNewPost(otherContent, response);			
		}		
		//mongoose.connection.close();
	});	
}

function addToExistingPass(pass, otherContent, response) {
	
	Store.findById(pass._id, function (err, passs) {
		
		if (err) console.log('Lookup Error: ' + err);
		
		passs.accounts.push({
			username: otherContent.username,
			password: otherContent.password
		});
		
		passs.save( function (err) {
			if (err) console.log('Save Error: ' + err);
			
			response.write('Password Saved');
			response.end();
			mongoose.connection.close();
		});		
	});
}

function saveNewPost(otherContent, response) {
	
	var store = new Store();
	store.name = (otherContent.name).toLowerCase();
	store.userSpelledName = otherContent.name;
	store.accounts.push({
		username: otherContent.username,
		password: otherContent.password
	});
	
	store.save(function (err) {
		if (err) console.log('Save Error: ' + err);
		
		response.write('Successfully saved: \n' + store);
		response.end();				
		mongoose.connection.close();
	});
}

function Get(otherContent, response) {
	console.log('In GET');
	
	// Find all passwords and return to client
	Store.find().lean().exec( function (err, pass) {
		
		response.writeHead(200, {"Content-Type": "application/json"});
		response.write(JSON.stringify(pass, null, 4));
		response.end();
		
		mongoose.connection.close();
	});	
}

function Delete(otherContent, response) {
	console.log('in delete');
	
	console.log('In delete with ' + JSON.stringify(otherContent));
	response.end();
}

function Update(otherContent, response) {
	console.log('In PUT');
	
	
	
	Store.findOne({ 'name': (otherContent.name).toLowerCase() }, function (err, pass) {
		
		if (err) console.log('ERROR:' + err);
		
		Store.findById(pass._id, function (err, passs) {
		
			if (err) console.log('Lookup Error: ' + err);
			
			// CHANGE PASSWORD HERE
			
			
			passs.save( function (err) {
				if (err) console.log('Save Error: ' + err);
				
				console.log('Password Updated');
				response.write('Password Updated');
				response.end();
			});		
		});
		
		mongoose.connection.close();
	});
}

function displayErrorMessage(response) {
	response.writeHead(404, {"Content-Type": "text/plain"});
	response.write("Denied boy");
	response.end();
}


exports.start = start;