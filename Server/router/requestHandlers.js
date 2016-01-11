var Store;
var mongoose;
Store = require('../model/store-schema'); 
mongoose = require('mongoose');
var encryption = require('encryption-module');


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
        console.log(str);
        
        // Decrypt data
        encryption.decrypt(str, function(error, decrypted) {
            
            if (error) console.log(error);
            
            else {
                // Turn decrypted back into JSON object
                var decrypt = JSON.parse(decrypted);
                
                // JSON.parse(string); --> string to JSON
                // JSON.stringify(obj); --> JSON to string
                //var newstr = JSON.parse(str);				
                
                // Verify correct POST data for access to server
                if (decrypt.Username === 'Brad' && decrypt.Password === '12345') {			
                    
                    // DO SECRET STUFF WITH CLIENT POST HERE 
                    if (decrypt.method === 'POST')
                        Post(decrypt.restOfContent, response);
                    else if (decrypt.method === 'GET')
                        Get(response);
                    else if (decrypt.method === 'DELETE')
                        Delete(decrypt.restOfContent, response);
                    else if (decrypt.method === 'PUT')
                        Update(decrypt.restOfContent, response);
                    
                } else {
                    // If tier1 auth was correct but username/password wrong
                    displayErrorMessage(response);
                }   
            }            
        });
	});		
}


function Post(otherContent, response) {
    
	console.log('In POST');
	response.writeHead(200, {'Content-Type': 'text/plan'});
    
    // Decrypt otherContent and turn into JSON object
    encryption.decrypt(otherContent, function(error, decrypted) {
        
        decrypted = JSON.parse(decrypted);
        
        // Query the db to see if the item exists already
        Store.findOne({ 'name': (decrypted.name).toLowerCase() }, function (err, pass) {
            
            if (err) console.log('ERROR:' + err);
            
            // If a document exists in db with name, check to see if username
            // already exists in that document
            if (pass) {
                            
                var exists = false;			
                for (var i = 0; i < pass.accounts.length; i++) {
                    if (pass.accounts[i].username === decrypted.username)
                        exists = true;
                }
                
                if (exists) {
                    console.log('Account Already Exists');
                    response.write('\nUsername Already Exists');
                    response.end();
                    mongoose.connection.close();
                } else {
                    // Add the new account credentials to the existing account name
                    addToExistingPass(pass, decrypted, response);				
                }
            }		 
            
            // If the password does not already exist add it to the db
            else {			
                saveNewPost(decrypted, response);			
            }	
        });
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
			
			else {
				console.log('Saved Successfully');
				response.write('New username ' + otherContent.username + ' added to existing account ' + passs.name);
				response.end();
				mongoose.connection.close();
			}			
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
		
		else {
			console.log('Saved Successfully');
			response.write('Successfully saved: \n' + store);
			response.end();				
			mongoose.connection.close();
		}		
	});
}

function Get(response) {
	console.log('In GET');
	
	// Find all passwords and return to client
	Store.find().lean().exec( function (err, pass) {
        
        // Encrypt data before sending
        encryption.encrypt(pass, function(error, encrypted) {
            
            // Print error to console and close mongoose connection
            if (error) {
                console.log(error);
                mongoose.connection.close();
            }
            
            // Send encrypted data to client
            else {
                response.writeHead(200, {"Content-Type": "application/json"});
                //response.write(JSON.stringify(pass, null, 4));
                response.write(encrypted);
                response.end();
                
                //console.log('Returned:\n' + JSON.stringify(pass, null, 4));
                console.log('Returned:\n' + encrypted);
                
                mongoose.connection.close();
            }
        });
	});	
}

function Delete(otherContent, response) {
	console.log('in DELETE');
	
	// Remove account from document
	if (otherContent.whatToDelete === 'account') {
		
		Store.update( { 'name': otherContent.name }, { $pull: 
			{ 'accounts': { 'username': otherContent.which }
		}},
		function (err) {
			if (err) console.log(err);
			
			else {
				response.write('Removed account ' + otherContent.which);
				response.end();
				
				mongoose.connection.close();
				
				console.log('Removed account ' + otherContent.which);
			}			
		});
	}
	
	// Remove whoel document from db
	else if (otherContent.whatToDelete === 'document') {
		
		Store.remove( { 'name': otherContent.name }, function(err) {
			if (err) console.log(err);
			
			else {
				response.write('Document Deleted');
				response.end();
				
				console.log('Document Deleted');
				
				mongoose.connection.close();				
			}			
		});
	}
}

function Update(otherContent, response) {
	console.log('In PUT');
	
	// Update the password
	if (otherContent.toChange === 'password') {
		Store.update({'name': otherContent.name, 'accounts.password': otherContent.old}, {'$set': {
			'accounts.$.password': otherContent.new
		}}, function (err) {
			if (err) console.log(err); 
			
			else {
				console.log('Password Changed to ' + otherContent.new);
			
				response.write('Password Changed to ' + otherContent.new);
				response.end();
				mongoose.connection.close();
			}			
		});
	} 
	
	// Update the username (probably not likely)
	else if (otherContent.toChange === 'username') {
		Store.update({'name': otherContent.name, 'accounts.username': otherContent.old}, {'$set': {
			'accounts.$.username': otherContent.new
		}}, function (err) {
			if (err) console.log(err); 
			
			else {
				console.log('Username Changed to ' + otherContent.new);
			
				response.write('Username Changed to ' + otherContent.new);
				response.end();
				mongoose.connection.close();	
			}			
		});
	}	
}

function displayErrorMessage(response) {
	response.writeHead(404, {"Content-Type": "text/plain"});
	response.write("Denied boy");
	response.end();
}


exports.start = start;