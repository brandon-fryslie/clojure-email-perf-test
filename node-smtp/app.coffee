# simple server to accept incoming emails

nodemailer = require 'nodemailer'

process.on('uncaughtException', (err) ->
  console.log(new Date())
  console.log('Caught exception: ' + err);
);

server_opts =
  debug: true

port = 25

cb = (req) ->
  console.log 'got me a mail!'
  req.pipe process.stdout
  req.accept()

console.log "Starting SMTP server on port #{port}"

# simple server
# smtp = nodemailer.createSimpleServer(server_opts, cb)
# smtp.listen port

# not simple server
smtp = nodemailer.createServer server_opts
smtp.listen port

smtp.on 'startData', (connection) ->
  console.log 'got connection', connection

smtp.on 'data', (connection, chunk) ->
  console.log 'got data', connection, chunk

smtp.on 'dataReady', (connection, callback) ->
  console.log 'got data ready', connection, callback
