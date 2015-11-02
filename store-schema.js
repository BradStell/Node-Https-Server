var mongoose = require('mongoose');

var Password = mongoose.Schema({
		name: String,
		accounts: []
	});
	
var PWmodel = mongoose.model('Store', Password);

module.exports = PWmodel;