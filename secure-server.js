var https = require('https');
var fs = require('fs');

var options = {
  key: fs.readFileSync('key.pem'),
  cert: fs.readFileSync('key-cert.pem')
};

https.createServer(options, function (req, res) {
	console.log("server hit");
  res.writeHead(200);
  res.end("hello world\n");
}).listen(8001);

console.log("Server Started");