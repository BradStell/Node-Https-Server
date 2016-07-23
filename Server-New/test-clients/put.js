var http = require('http');

var options = {
    host: '127.0.0.1',
    path: '/',
    port: '3000',
    method: 'PUT',
    headers: {
        'Content-Type': 'application/json',
        'stage-1-auth': '12345'
    }
};

var req = http.request(options, (res) => {
    let message = '';
    res.on('data', (chunk) => {
        message += chunk;
    });

    res.on('end', () => {
        console.log(`Message from server = ${message}`);
    });
});

req.write('This is a message');
req.end();