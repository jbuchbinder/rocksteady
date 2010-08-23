from amqplib import client_0_8 as amqp
import random
import setting
import re

def signal_handler(signal, frame):
  print 'Exiting connection'
  chan.close()
  conn.close()
  sys.exit(0)

print 'Queue: ' + setting.QUEUE
conn = amqp.Connection(host=setting.SERVER + ":" + setting.PORT, userid=setting.USER, password=setting.PASS, virtual_host=setting.VHOST, insist=False)
chan = conn.channel()
chan.queue_declare(queue=setting.QUEUE, durable=False, exclusive=False, auto_delete=True)
chan.exchange_declare(exchange=setting.EXCHANGE, type="fanout", durable=False, auto_delete=False,)

chan.queue_bind(queue=setting.QUEUE, exchange=setting.EXCHANGE, routing_key="")

def recv_callback(msg):
    if re.match('.*',msg.body):
        print 'Received: ' + msg.body
#    print msg.properties

chan.basic_consume(queue=setting.QUEUE, no_ack=True, callback=recv_callback, consumer_tag="testtag")
while True:
    chan.wait()
chan.basic_cancel("testtag")


