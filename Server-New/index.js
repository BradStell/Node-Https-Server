const express = require('express');
const route = require('./route');
const app = express();

// authenticate request before passing on
let auth = (req, res, next) => {
    console.log('in auth');

    if (req.headers['stage-1-auth'] === '12345') {
        console.log('you have been authenticated');
        res.write('you have been authenticated\n');
        next();    
    } else {
        console.log('authentication required');
        res.write('authentication required');
        res.end();
    }
};

// GET
app.get('/', auth, (req, res) => {
    route.GET(req, res);
});

// POST
app.post('/', auth, (req, res) => {
    route.POST(req, res);
});

// PUT
app.put('/', auth, (req, res) => {
    route.PUT(req, res);
});

// DELETE
app.delete('/', auth, (req, res) => {
    route.DELETE(req, res);
});


// Start server
app.listen(3000, () => {
    console.log(`Server listening on http://localhost:3000`);
});