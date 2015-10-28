function route (handle, pathname, newstr) {
	console.log("routing " + pathname);
	
	if (typeof handle[pathname] === 'function') {
		handle[pathname](newstr);
		
	} else {
		console.log('Not a valid path');
	}
}

exports.route = route;