
<h2 align="center">Networking portion</h2>

Tested using python 3.9, do not run this within IDLE.


<h3 align="center">Quick rundown of the process that the client server goes through in this current itteration: <h3 align="center">

1. Server opens and listens on a port (Default: 8080)
2. Client connects to the server through that port
3. The Server opens a new thread dedicated to that user.
4. The Server gives a new port for the client to connect to (EG: 8081) and begins listening on that port
5. Client recieves this information and closes the connection, it then connects to that port.
6. Within that thread, another thread is opened specifically for sending messages to that cleint, whilst the parent thread recieves messages from this client.


