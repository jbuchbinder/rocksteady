import random
   
SERVER = "localhost"
PORT = "5672"

VHOST = "/"
USER = "guest"
PASS = "guest"
EXCHANGE = "rocksteady"

QUEUE = "mytest" + str(random.randint(1,100))

