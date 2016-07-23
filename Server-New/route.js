let auth = (req, res) => {
    let str = '';
    
    req.on('data', (chunk) => {
        str += chunk;
    });

    req.on('end', () => {
        console.log(str);
    });    
};


///////////////////////////////////////////////////////

let GET = (req, res) => {
    auth(req, res);
};

let POST = (req, res) => {

};

let PUT = (req, res) => {

};

let DELETE = (req, res) => {

};

exports.GET = GET;
exports.POST = POST;
exports.PUT = PUT;
exports.DELETE = DELETE;