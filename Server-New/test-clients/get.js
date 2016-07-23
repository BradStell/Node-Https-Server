const http = require('http');

let options = {
    host: '127.0.0.1',
    path: '/',
    port: '3000',
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
        'stage-1-auth': '12345'
    }
};

let req = http.request(options, (res) => {
    let message = '';
    res.on('data', (chunk) => {
        message += chunk;
    });

    res.on('end', () => {
        console.log(`Message from server = ${message}`);
    });
});

let body = {
    username: 'brad',
    password: 'pa$$word'
};

req.write(JSON.stringify(body));
req.end();