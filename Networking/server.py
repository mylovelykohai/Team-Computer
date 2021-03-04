import socket
import sys
import time
import threading

def clientSendThread(connection,user):
   messageTotal = len(messageArray) # set total message to equal the current number of messages sent
   while True:
      if messageTotal !=  len(messageArray): # if a new message has been sent
         sendMessage = messageArray[len(messageArray) - 1].encode() # encode the last sent message
         connection.send(sendMessage) # send the client the message
         messageTotal += 1 # increment message count
         
def clientThread(connection,user):
   newPort = mainPort + clientID + 1 # new port for the users session
   clientPort = socket.socket() # create that port
   clientPort.bind((h_name, newPort)) 
   newPort = str(newPort) # convert and encode
   newPort = newPort.encode()
   connection.send(newPort) # tell the client which new port to connect to, the client will sever the connection here.
   clientPort.listen(1) # wait for the client to reconnect 
   connection,address = clientPort.accept()
   print(address, " Has connected to the chatroom and is now online...")

   sendThread = threading.Thread(target = clientSendThread, args = (connection,username,)) # Create a thread for sending messages to the user
   sendThread.daemon = True; # daemon to true, thread will die when the main process does

   sendThread.start() # start
   while True:
      try:
         in_message = connection.recv(1024) # wait for a message to be recieved
      except ConnectionResetError: # if user closed client
         clientPort.close() # close port
         print("User",user,"has disconnected",)
         break # leave loop
      in_message = in_message.decode('utf-8')
      recvMessage = user + ": " + in_message 
      messageArray.append(recvMessage) # add message to the message aray
      print(recvMessage) # print message to concole

   print("Thread for user ID",clientID, "on port", newPort.decode('utf-8'),"closed, port is now free.") # announce thread termination



sock = socket.socket() # Create Socket
h_name = socket.gethostname()

print("Server will start on host: ", h_name)
mainPort = 8080 # Default Listen Port
sock.bind((h_name, mainPort))
print("Binded host and port successfully")
print("Server is waiting for incoming connections...")
clientID = 0 # ID of the next client to connect
clientRecvThreads = [] # Array of threads for clients.
messageArray = [] # Array of total messages sent

while True:
   sock.listen(1) # Server will wait here for a connection
   connection,address = sock.accept()
   username = connection.recv(1024) # Recieve the username of the user
   username = username.decode('utf-8')
   print(username, "(",address,") ", " Has connected to the login server...")
   
   clientRecvThreads.append(threading.Thread(target=clientThread, args=(connection,username,))) # Create a thread for the user's session
   clientRecvThreads[clientID].daemon = True; # daemon to true, thread will die when the main process does
   clientRecvThreads[clientID].start() # start thread

   clientID += 1
