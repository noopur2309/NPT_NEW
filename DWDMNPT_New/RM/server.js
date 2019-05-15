var express=require('express');
var  app=express();
var http =require('http')
http.Server(app).listen(8081);
console.log('server started');
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.post("/client",function(request,response){


    var x="data successfully recieved by server";
    response.writeHead(200, {'Content-Type': 'application/JSON'});
    response.write(x);
    response.end();
    algo(request.body);
});


var events = require('events');
var request = require('request');
async function algo(x){
let data=x;

const {c, cpp, node, python, java} = require('compile-run');
let resultPromise = cpp.runFile('./rm.cpp', { stdin:JSON.stringify(data)});

resultPromise
    .then(result => {
        //var res=JSON.parse(result);

         request({
            url: 'http://127.0.0.1:8082/pro',
            method: 'POST',
            json: result,
          }, function(error, response, body){
            console.log(body);
          });
    })
    .catch(err => {

        console.log(err);
    });

}
