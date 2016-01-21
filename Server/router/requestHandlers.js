// Module Imports
var Store = require('../model/store-schema'); 	// mongodb/mongoose schema
var mongoose = require('mongoose');				// mongoose module
var jbs_crypto = require('jbs-crypto');			// encryption module


/**
 *	Function reachable from outside code that requests this module.
 *	 - opens mongodb connection to database 'UserCache'
 *	 - decrypts response from client
 *	 - routes request to appropriate crud method
 */
function start(request, response) {
	
	// Open mongodb connection to 'UserCache' database
	mongoose.connect('mongodb://127.0.0.1/UserCache');
	
	// Make sure db connection is good
	var db = mongoose.connection;
	db.on('error', console.error.bind(console, 'Connection error:'));
	db.once('open', function (callback) {
		console.log('Database opened');
	});
	
	//Capture POST data sent from client with request emitters
	var str = '';

	// concatenate incoming data from request
	request.on('data', function(chunk) {
		str += chunk;
	});
	
	// all data has been recieved from client
	request.on('end', function() {

		// Show enctypted output to console
        console.log(str);
        
        // Decrypt data
        jbs_crypto.decrypt(str, function(error, decrypted) {
            
            if (error) console.log(error);
            
            else {
                // Turn decrypted string back into JSON object
                var data = JSON.parse(decrypted);

                // Verify correct POST data for access to server
                if (data.Username === 'Brad' && data.Password === '12345') {			
                    
                    // Route program to appropriate method function (GET, PUT, POST, DELETE)

                    if (data.method === 'POST')
                        Post(data.restOfContent, response);

                    else if (data.method === 'GET')
                        Get(response);

                    else if (data.method === 'DELETE')
                        Delete(data.restOfContent, response);

                    else if (data.method === 'PUT')
                        Update(data.restOfContent, response);
                    
                } else {
                    // If tier1 auth was correct but username/password wrong
                    displayErrorMessage(response);
                } 
            }
        });
	});		
}


/**
 *	CRUD operation Create
 *	 - Will create new entity with account
 *	 - Will create new account with existing entity
 */
function Post(otherContent, response) {
    
	console.log('In POST');
	response.writeHead(200, {'Content-Type': 'text/plan'});
        
    // Query the db to see if the entity exists or not
    Store.findOne({ 'name': (otherContent.name).toLowerCase() }, function (err, pass) {
        
        if (err) console.log(err);
        
        // There already exists a document (entity) with the name
        if (pass) {
                        
            // Check the account name and see if it already exists or not
            var exists = false;			
            for (var i = 0; i < pass.accounts.length; i++) {
                if (pass.accounts[i].username === otherContent.username)
                    exists = true;
            }
            
            // Tell user the account in the entity already exists
            if (exists) {
                
                var str = 'Username ' + otherContent.username + ' Already Exists for Account ' + pass.userSpelledName;
                console.log(str);

                // Encrypt the response back to the client                
                jbs_crypto.encrypt(str, function(err, encrypted) {

                	if (err) console.log(err);

                	else {
                		response.write(encrypted);
		                response.end();
                	}	                	
                });
	                
                mongoose.connection.close();
            } 

            // Add the new account credentials to the existing entity name
            else {                
                addToExistingPass(pass, otherContent, response);				
            }
        }		 
        
        // If the entity does not already exist, create a new entry
        else {			
            saveNewPost(otherContent, response);			
        }	
    });
}

/**
 *	Adds new account to existing entity
 */
function addToExistingPass(pass, otherContent, response) {

	// Add another account object to the accounts array
	pass.accounts.push({
		username: otherContent.username,
		password: otherContent.password
	});
	
	// Save the document back to the database
	pass.save( function (err) {
		if (err) console.log('Save Error: ' + err);
		
		else {			
			var str = 'New username ' + otherContent.username + ' added to existing account ' + pass.name;
			console.log(str);

			// Encrypt the response back to the client
			jbs_crypto.encrypt(str, function(err, encrypted) {
				if (err) console.log(err);

            	else {
            		response.write(encrypted);
	                response.end();
            	}	
			});
			mongoose.connection.close();
		}			
	});	
}

/**
 *	Adds new entity (document) to the db
 */
function saveNewPost(otherContent, response) {
	
	// Create new document for database, new account being created
	var store = new Store();

	// Fill in the fields with the entity details
	store.name = (otherContent.name).toLowerCase();
	store.userSpelledName = otherContent.name;
	store.accounts.push({
		username: otherContent.username,
		password: otherContent.password
	});
	
	// Save to the database
	store.save(function (err) {
		if (err) console.log('Save Error: ' + err);
		
		else {			
			var str = 'Created account ' + otherContent.name + ' with username ' + otherContent.username;
			console.log(str);

			// Encrypt response back to client
			jbs_crypto.encrypt(str, function(err, encrypted) {
				if (err) console.log(err);

            	else {
            		response.write(encrypted);
	                response.end();
            	}	
			});
			mongoose.connection.close();
		}		
	});
}

/**
 *	CRUD operaction Read
 *	 - Returns all documents in database
 */
function Get(response) {
	console.log('In GET');
	
	// Find all passwords and return to client
	Store.find().lean().exec( function (err, pass) {

		if (err) console.log(err);

		else {
			// Encrypt data before sending
	        jbs_crypto.encrypt(pass, function(error, encrypted) {
	            
	            // Print error to console
	            if (error) console.log(error);
	            
	            // Send encrypted data to client
	            else {
	                response.writeHead(200, {"Content-Type": "text/plain"});
	                response.write(encrypted);
	                response.end();
	                
	                console.log('Returned:\n' + encrypted);
	            }
	            mongoose.connection.close();
	        });
		} 
	});	
}

/**
 *	CRUD operation Delete
 *	 - Deletes account from existing entity
 *	 - Removes entire entity (document)
 */
function Delete(otherContent, response) {
	console.log('in DELETE');
	
	// Remove account from document
	if (otherContent.whatToDelete === 'account') {

		// Find the document (entity) with the specified name
		Store.findOne({ 'name': (otherContent.name).toLowerCase() }, function (err, pass) {

			if (err) console.log('ERROR:' + err);
        
	       // If document does exist, check to see if account exists
	        if (pass) {
	                        
	            var exists = false;
	            var where = -1;		// Spot in array where account exists

	            // Locate account in accounts array
	            for (var i = 0; i < pass.accounts.length; i++) {
	                if (pass.accounts[i].username === otherContent.username) {	                	
	                    exists = true;
	                    where = i;
	                    break;
	                }
	            }
	            
	            // If account with username does exist, delete it
	            if (exists) {

	            	console.log('Removed ' + JSON.stringify((pass.accounts.splice(where, 1))) );

	            	pass.save(function(err) {
	            		if (err) console.log(err);

	            		else {
	            			var str = 'Removed user account ' + otherContent.username + ' from account ' + otherContent.name;
	            			console.log(str);

	            			jbs_crypto.encrypt(str, function(err, encrypted) {
	            				if (err) console.log(err);

	            				else {
	            					response.write(encrypted);
					                response.end();
	            				}		            				
	            			});
		            			
			                mongoose.connection.close();
	            		}	            			
	            	});
	            } 

	            // If username does not exist, inform user
	            else {
	            	var str = 'Username ' + otherContent.which + ' does not exist in account ' + otherContent.name;
	            	console.log(str);

	            	jbs_crypto.encrypt(str, function(err, encrypted) {
        				if (err) console.log(err);

        				else {
        					response.write(encrypted);
			                response.end();
        				}		            				
        			});

	                mongoose.connection.close();
	            }
	        }		 
	        
	        // IF document does not exist, inform user
	        else {
	        	var str = 'No document with ' + otherContent.name;
            	console.log(str);

            	jbs_crypto.encrypt(str, function(err, encrypted) {
    				if (err) console.log(err);

    				else {
    					response.write(encrypted);
		                response.end();
    				}		            				
    			});
    			
                mongoose.connection.close();	
	        }
		});
	}
	
	// Remove whoel document from db
	else if (otherContent.whatToDelete === 'document') {
		
		Store.remove( { 'name': (otherContent.name).toLowerCase() }, function(err, removed) {
			if (err) console.log(err);
			
			else {
				var str = '';

				if (removed.result.n == 0) {
					str = 'Something wrong, no document removed';
				} else {
					str = 'Removed document ' + otherContent.name;
				}

				jbs_crypto.encrypt(str, function(err, encrypted) {
					if (err) console.log(err);

					else {
						response.write(encrypted);
						response.end();
					}
				});
				
				mongoose.connection.close();
			}			
		});
	}

	// If server was called incorrectly
	else {

		var str = 'You must specify an "account" or a "document" for removal in the "whatToDelete" field';
		console.log(str);

		jbs_crypto.encrypt(str, function(err, encrypted) {
			if (err) console.log(err);

			else {
				response.write(encrypted);
				response.end();	
			}			
		});		
		
		mongoose.connection.close();
	}
}


/**
 *	CRUD operation Update
 *	 - Change password
 *	 - Change account username (rare)
 */
function Update(otherContent, response) {
	console.log('In PUT');
	var str = '';
	
	// Update the password
	if (otherContent.toChange === 'password') {

		Store.update({'name': (otherContent.name).toLowerCase(), 'accounts.password': otherContent.old, 'accounts.username': otherContent.username},
			{'$set': {'accounts.$.password': otherContent.new} },
			function (err, resp) {
			
				if (err) console.log(err);
				
				else if (resp.nModified === 0) {

					str = 'No change made. Make sure entity name, ' +
						'old password, and account username are spelled correctly';

				} else {

					str = 'Entity ' + otherContent.name + ', account ' + otherContent.username + 
						' password changed to ' + otherContent.new;
				}

				console.log(str);

				jbs_crypto.encrypt(str, function(err, encrypted) {
					if (err) console.log(err);

					else {
						response.write(encrypted);
						response.end();
					}						
				});
					
				mongoose.connection.close();
			});
	} 
	
	// Update the username (probably not likely)
	else if (otherContent.toChange === 'username') {

		Store.update({'name': (otherContent.name).toLowerCase(), 'accounts.username': otherContent.username}, 
			{'$set': {'accounts.$.username': otherContent.new}}, 
			function (err, resp) {
				if (err) console.log(err); 

				else if (resp.nModified === 0) {

					str = 'No change made. Make sure entity name and ' +
						'account username are spelled correctly';

				} else {
					str = 'Entity ' + otherContent.name + ', account ' + otherContent.username + 
						' changed to ' + otherContent.new;
				}

				console.log(str);

				jbs_crypto.encrypt(str, function(err, encrypted) {
					if (err) console.log(err);

					else {
						response.write(encrypted);
						response.end();
					}
				});
						
				mongoose.connection.close();
		});
	}

	else {
		str = 'You must specify either an "account" or "username" to change';

		console.log(str);

		jbs_crypto.encrypt(str, function(err, encrypted) {
			if (err) console.log(err);

			else {
				response.write(encrypted);
				response.end();
			}
		});

		mongoose.connection.close();
	}
}

function displayErrorMessage(response) {
	response.writeHead(404, {"Content-Type": "text/plain"});
	response.write('404 page not found');
	response.end();
}


// Export the start function as a module
exports.start = start;