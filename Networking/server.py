import socket
import ssl

import sys
import time
import threading
import json 
import urllib

import mysql.connector
from mysql.connector import pooling

from datetime import datetime

import uuid 

config = {
  'pool_name': 'pool',
  'pool_reset_session': True,
  'pool_size' : 32,
  'user': 'root',
  'password': 'PhpMyAdmin!23',
  'host': '127.0.0.1',
  'database': 'MASTER',
  'raise_on_warnings': True
}


def runDBCommand(command):
   t = con.get_connection()
   tCur = t.cursor() 
   
   tCur.execute(command)
   result = tCur.fetchone()

   t.commit()

   tCur.close()
   t.close()

   return(result)

def parsePacket(packet, cID):
    packet = urllib.parse.unquote(packet)
    print(packet)
    packetDict = json.loads(packet)
    status = 5

    if(packetDict["pt"] == "l"): # yandere dev mode, where be switch statements huh
        login(packetDict, cID)

    if(packetDict["pt"] == "m"):
        message(packetDict, cID)      

    if(packetDict["pt"] == "s"):
        signup(packetDict,cID)

    if(packetDict["pt"] == "du"):
        deleteUser(packetDict, cID)

def deleteUser(pPacket, cID):
    Email = pPacket["Email"]
    sessionID = pPacket["sessionID"]

    result = runDBCommand('''SELECT EXISTS(SELECT * from User WHERE Email = "%s" AND sessionID = "%s")''' % (Email, sessionID))

    if(result[0]):
        userID = runDBCommand('''SELECT userID from User WHERE Email = "%s"''' % Email)[0]
        runDBCommand('''UPDATE Message SET MessageBody = NULL WHERE userID = %d ''' % userID)
        runDBCommand('''UPDATE User SET FullName = "DELETED USER", Email = "no@no.com", DOB = NULL, ROLE = NULL, password = NULL, salt = NULL, is_active = NULL, sessionID = NULL WHERE userID = %d''' % userID)

    packetJSON[cID] = '{"pt" : "dr", "status" : 1}\n'
    packetToSend[cID] = True; 

def signup(pPacket, cID):
    Email = pPacket["Email"]
    password = pPacket["password"]

    result = runDBCommand('''SELECT EXISTS(SELECT * from User WHERE Email = "%s")''' % Email)

    if(result[0] != 1):
        runDBCommand('''INSERT INTO User (FullName, Email, password) VALUES ("%s", "%s", "%s");''' % ("noName", Email, password))
    packetJSON[cID] = '{"pt" : "sr", "status" : 1}\n'
    packetToSend[cID] = True; 

def login(pPacket, cID):
    global packetJSON
    global packetToSend
    global packetToSendID

    Email = pPacket["Email"]
    password = pPacket["password"]

    result = runDBCommand('''SELECT EXISTS(SELECT * from User WHERE email = "%s" AND password = "%s")''' % (Email, password))
    sessionID = "";

    if (result[0]):
        sessionID = uuid.uuid1()
        runDBCommand('''UPDATE User SET sessionID = "%s" WHERE email = "%s";''' % (str(sessionID), Email))
   
    packetJSON[cID] = '{"pt" : "lr", "sessionID" : "%s"}\n' % sessionID 
    packetToSend[cID] = True

    #print("pain", packetJSON)
    #print("pain", packetToSend)
    #print("packetJSON", packetJSON[cID])
    #print("packetToSend", packetToSend[cID])
    print(cID)
           
def message(pPacket, cID):
    global tMessages

    ConvID = pPacket["convID"]
    message = pPacket["message"]
    Email = pPacket["Email"]
    sessionID = pPacket["sessionID"]
    emotion = pPacket["emotion"]

    result = runDBCommand('''SELECT EXISTS(SELECT * from User WHERE Email = "%s" AND sessionID = "%s")''' % (Email, sessionID))

    if(result[0]):
         now = datetime.now()
         userID = runDBCommand('''SELECT userID from User WHERE Email = "%s"''' % Email)[0]
         epoch = now.strftime('%Y-%m-%d %H:%M:%S') 
         runDBCommand('''INSERT INTO Message (UserID, ConvID, MessageBody, CreateDate, emotion) values (%d, %d,"%s","%s","%s");''' % (userID, ConvID, message, epoch, emotion)) 
         tMessages += 1
         print(Email, ": ", message)


    else:
        print(Email, "failed sessionID check.")

def clientSendThread(connection, cID):
   global packetToSend
   global packetJSON 
   global tMessages

   messageTotal = tMessages # set total message to equal the current number of messages sent
  
   while True:
      if packetToSend[cID] == True: # if a new message has been sent
         print(packetToSend)
         print("there is a packet to send", cID)        
         try:
            sendMessage = packetJSON[cID].encode() # encode the last sent message
            connection.send(sendMessage) # send the client the message
         except (ConnectionResetError, ConnectionAbortedError) as e:
             break

         packetToSend[cID] = False

      if tMessages != messageTotal:

         lastMessageQuery = runDBCommand('''SELECT * FROM Message WHERE MessageID = %d;''' % (messageTotal + 1))
         lastMessage = lastMessageQuery[3]
         ConvID = lastMessageQuery[2]
         emotion = lastMessageQuery[5]
         lastMessageUsername = runDBCommand('''SELECT FullName FROM User WHERE UserID = %d;''' % (int(lastMessageQuery[1])))[0]
         
         packetJSON[cID] = '{"pt" : "m", "username" : "' + lastMessageUsername + '", "message": "' + lastMessage + '", "ConvID": "' + str(ConvID) + '"}\n'


         for i in range(len(packetToSend)):
            packetToSend[i] = True;
         for i in range(len(packetJSON)):
            packetJSON[i] = '{"pt" : "m", "username" : "' + lastMessageUsername + '", "message": "' + lastMessage + '", "ConvID": "' + str(ConvID) + '", "emotion": "' + emotion + '"}\n'

         messageTotal = tMessages


   
 
def clientThread(connection, address, cID):
   global tMessages

   sendThread = threading.Thread(target = clientSendThread, args = (connection, cID,)) # Create a thread for sending messages to the user
   sendThread.daemon = True; # daemon to true, thread will die when the main process does
   sendThread.start() # start

   while True:
      try:
         inPacket = connection.recv(1024) # wait for a message to be recieved

      except (ConnectionResetError, ConnectionAbortedError) as e: # if user closed client
         message = (address[0] + " has disconnected.")
         print(message)
         break # leave loop

      inPacket = inPacket.decode('utf-8')

      if inPacket != "":
         parsePacket(inPacket, cID)           
      else:
         break

   print("Thread for user ID", cID, "terminating.") # announce thread termination

sock = socket.socket() # Create Socket
h_name = socket.gethostname()

print("Server will start on host: ", h_name)
mainPort = 7777 # Default Listen Port
sock.bind((h_name, mainPort))

print("Binded host and port successfully")
print("Server is waiting for incoming connections...")

con = pooling.MySQLConnectionPool(**config)

packetToSend = [];
packetJSON = [];

clientID = 0 # ID of the next client to connect
clientRecvThreads = [] # Array of threads for clients.
tMessages = runDBCommand('''SELECT Count(*) FROM Message;''')[0]

print(tMessages)

while True:
   sock.listen(1) # Server will wait here for a connection
   connection, address = sock.accept()

   print(address, " Has connected to the  server...")

   packetToSend.append(False)
   print("packetToSend KILL ME", packetToSend)
   packetJSON.append("")

   clientRecvThreads.append(threading.Thread(target = clientThread, args = (connection, address, clientID,))) # Create a thread for the user's session
   clientRecvThreads[clientID].daemon = True; # daemon to true, thread will die when the main process does
   clientRecvThreads[clientID].start() # start thread

   clientID += 1
