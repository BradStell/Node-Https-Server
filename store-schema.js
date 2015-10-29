var mongoose = require('mongoose');

var Password = mongoose.Schema({
		name: String,
		username: String,
		password: String
	});
	
var PWmodel = mongoose.model('Store', Password);

module.exports = PWmodel;