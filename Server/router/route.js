function route (handle, pathname, request, response) {
	console.log("routing " + pathname);
	
	if (typeof handle[pathname] === 'function') {
		handle[pathname](request, response);
		
	} else {
		console.log('Not a valid path');
	}
}

exports.route = route;