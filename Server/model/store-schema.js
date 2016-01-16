var mongoose = require('mongoose');



var Password = mongoose.Schema({
	'name': String,
	'userSpelledName': String,
	'accounts': Array
});
	
var PWmodel = mongoose.model('Store', Password);

module.exports = PWmodel;