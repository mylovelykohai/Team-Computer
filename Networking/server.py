import socket
import sys
import time
import threading

import sqlite3

def clientSendThread(connection, user, tCur):
   messageTotal = tMessages # set total message to equal the current number of messages sent

   while True:
      if messageTotal !=  tMessages: # if a new message has been sent

         lastMessage = tCur.execute('''SELECT * FROM tbl1 WHERE rowid = ?;''', [messageTotal + 1])
         lastMessage = tCur.fetchall()[0] # 1D array inside of an tuple lol
         lastMessage = (lastMessage[1] + ": " + lastMessage[2])
         try:
	         sendMessage = lastMessage.encode() # encode the last sent message
	         connection.send(sendMessage) # send the client the message
         except ConnectionResetError:
             break;	      

         messageTotal += 1 # increment message count
 
def clientThread(connection, user):
   global tMessages
   tCur = con.cursor()

   sendThread = threading.Thread(target = clientSendThread, args = (connection,username,tCur)) # Create a thread for sending messages to the user
   sendThread.daemon = True; # daemon to true, thread will die when the main process does
   sendThread.start() # start


   while True:
      epoch = round(time.time())

      try:
         in_message = connection.recv(1024) # wait for a message to be recieved

      except ConnectionResetError: # if user closed client
         message = (" User " + user + " has disconnected.")
         print(message)
         tCur.execute('''INSERT INTO tbl1 VALUES (?, ?, ?);''', [epoch, "SERVER", message]) #SQL command to run
         tMessages += 1 
         break # leave loop
         
      in_message = in_message.decode('utf-8')
      recvMessage = user + ": " + in_message 


      tCur.execute('''INSERT INTO tbl1 VALUES (?, ?, ?);''', [epoch, user, in_message]) #SQL command to run
      con.commit() # commit it to the database
      tMessages += 1 # add message to the message array

      print(recvMessage) # print message to concole

   print("Thread for user ID", clientID, "terminating.") # announce thread termination

sock = socket.socket() # Create Socket
h_name = socket.gethostname()

print("Server will start on host: ", h_name)
mainPort = 1200 # Default Listen Port
sock.bind((h_name, mainPort))
print("Binded host and port successfully")
print("Server is waiting for incoming connections...")

try:
  con = sqlite3.connect('test.db', check_same_thread = False)
  print("Connected to database")
  cur = con.cursor()

except FileNotFoundError:
  print("Database not found")

clientID = 0 # ID of the next client to connect
clientRecvThreads = [] # Array of threads for clients.
tMessages = cur.execute('''SELECT Count(*) FROM tbl1;''')
tMessages = cur.fetchone()[0]
print(tMessages)


while True:
   sock.listen(1) # Server will wait here for a connection
   connection,address = sock.accept()
   username = connection.recv(1024) # Recieve the username of the user
   username = username.decode('utf-8')
   print(username, "(",address,") ", " Has connected to the  server...")
   
   clientRecvThreads.append(threading.Thread(target = clientThread, args = (connection,username,))) # Create a thread for the user's session
   clientRecvThreads[clientID].daemon = True; # daemon to true, thread will die when the main process does
   clientRecvThreads[clientID].start() # start thread

   clientID += 1
