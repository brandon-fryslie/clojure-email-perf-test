# simple server to accept incoming emails

SMTPServer = require('smtp-server').SMTPServer
microtime = require 'microtime'
_ = require 'lodash'

process.on('uncaughtException', (err) ->
  console.log(new Date())
  console.log('Caught exception: ' + err);
);

port = 25
host = 'localhost'

email_counter = 0
start_time = null
last = null

reset_timer = ->
  console.log "Got #{email_counter}"
  console.log "Elapsed time: #{last - start_time}"

  email_counter = null
  start_time = null
  last = null

debounced_timer = _.debounce reset_timer, 1000

server = new SMTPServer
  disabledCommands: ['AUTH']
  logger: info: (->), debug: (->), error: ->
  onData: (stream, session, callback) ->
    t = microtime.nowDouble()
    start_time ?= t
    last = t
    email_counter++
    if email_counter % 100 is 0
      console.log "Got #{email_counter} emails in #{last - start_time} seconds"

    debounced_timer()

    callback()
    stream.on 'data', ->


console.log "Starting SMTP server on #{host}:#{port}"
server.listen port, host
