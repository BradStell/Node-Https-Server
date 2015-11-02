var mongoose = require('mongoose');

var Password = mongoose.Schema({
		name: String,
		userSpelledName: String,
		accounts: []
	});
	
var PWmodel = mongoose.model('Store', Password);

module.exports = PWmodel;