
<h2 align="center">Networking portion</h2>

Tested using python 3.9, and sqlite3 2.6.0. Do not run this within IDLE.

<h3 align="center">Quick rundown of the process that the client server goes through in this current itteration: </h3>

1. Server opens and listens on a port (Default: 1200)
2. Client connects to the server through that port
3. The Server opens a new thread dedicated to that user.
4. Within that thread, another thread is opened specifically for sending messages to that cleint, whilst the parent thread recieves messages from this client.



