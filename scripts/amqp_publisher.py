#!/bin/env python
#
# Mark Lin
# Produce metric for CEP testing
#

from amqplib import client_0_8 as amqp
import random
import sys, signal, time
import setting

def signal_handler(signal, frame):
  print 'Exiting connection'
  chan.close()
  conn.close()
  sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)

COLLECT_INTERVAL = 5
METRIC_PREFIX2 = "1min.admixer.common.revision"
METRIC_PREFIX = "1min.admixer.requests.request_stats.99th"
COLO_LIST = ['sc9','dl2','am1']
HOST_LIST = ['prf101','prf102','prf103']
#COLO_LIST = ['dl2']
OST_LIST = ['mxr200']

conn = amqp.Connection(host=setting.SERVER + ":" + setting.PORT, userid=setting.USER, password=setting.PASS, virtual_host="/", insist=False)
chan = conn.channel()

i = 0

# Now goes through each colo and output message
while True:
  timestamp = str(int(time.time()))
  msg=''
  for colo in COLO_LIST:
    for host in HOST_LIST:
    
      key = METRIC_PREFIX + "." + colo + "." + host
      value = str(random.randint(50,200))
#      value = "200"
      msg_raw = key + " " + value + " " + timestamp + "\n"
      msg = amqp.Message(msg_raw)
      print msg_raw
      chan.basic_publish(msg,exchange=setting.EXCHANGE)

      key = METRIC_PREFIX2 + "." + colo + "." + host
      value = str(random.randint(50,200))
      msg_raw = key + " " + value + " " + timestamp + "\n"
      msg = amqp.Message(msg_raw)
#      print msg_raw
#      chan.basic_publish(msg,exchange=setting.EXCHANGE)

      i = i + 1
  time.sleep(COLLECT_INTERVAL)


