import socket # for sockets

import time # time functions
import threading # allows us to run more than one thread
import json # easy json manipulation
import urllib # percent code shenanigans 

import mysql.connector
from mysql.connector import pooling # for database interactions

from datetime import datetime # for storing dates of messages

import uuid # for generating sessionID


#kieran's code

import hashlib, binascii, os 

def hash_password(password):
    """Hash a password for storing."""
    salt = hashlib.sha256(os.urandom(60)).hexdigest().encode('ascii')
    pwdhash = hashlib.pbkdf2_hmac('sha512', password.encode('utf-8'), salt, 100000)
    pwdhash = binascii.hexlify(pwdhash)
    return (salt + pwdhash).decode('ascii'), salt.decode('ascii')

def verify_password(stored_password, provided_password, salt):
    """Verify a stored password against one provided by user"""
    salt = salt[:64]
    stored_password = stored_password[64:]
    pwdhash = hashlib.pbkdf2_hmac('sha512', provided_password.encode('utf-8'), salt.encode('ascii'), 100000)
    pwdhash = binascii.hexlify(pwdhash).decode('ascii')
    return pwdhash == stored_password

#kieran's code end

config = { # configs for the mySQL database system, update 'user', 'password' and database with what ever you set them as.
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
   t = con.get_connection() # create pooled connection and create cursor
   tCur = t.cursor()  
    
   tCur.execute(command) # execute command and grab the result
   result = tCur.fetchone() 

   t.commit() # commit any changes

   tCur.close() # close pool and cursor
   t.close()

   return(result) # return result if one was provided.

def parsePacket(packet, cID):
    packet = urllib.parse.unquote(packet) # remove percent encoding caused by message transit.
    packetDict = json.loads(packet) # convert JSON string into dictonary
    status = 5

    if(packetDict["pt"] == "l"): # BTEC switch statement because python doesnt have it, is based on the packet 'type'
        login(packetDict, cID)

    if(packetDict["pt"] == "m"):
        message(packetDict, cID)      

    if(packetDict["pt"] == "s"):
        signup(packetDict,cID)

    if(packetDict["pt"] == "du"):
        deleteUser(packetDict, cID)

def deleteUser(pPacket, cID):

    Email = pPacket["Email"] # grab email and sessionID from packet
    sessionID = pPacket["sessionID"]

    result = runDBCommand('''SELECT EXISTS(SELECT * from User WHERE Email = "%s" AND sessionID = "%s")''' % (Email, sessionID)) # check if the user is logged in

    if(result[0]):
        userID = runDBCommand('''SELECT userID from User WHERE Email = "%s"''' % Email)[0] # get userID 
        runDBCommand('''UPDATE Message SET MessageBody = NULL WHERE userID = %d ''' % userID) # delete all messages by setting to NULL and delete user by setting most fields to NULL
        runDBCommand('''UPDATE User SET FullName = "DELETED USER", Email = "no@no.com", DOB = NULL, ROLE = NULL, password = NULL, salt = NULL, is_active = NULL, sessionID = NULL WHERE userID = %d''' % userID)

    packetJSON[cID] = '{"pt" : "dr", "status" : 1}\n' # return status packet
    packetToSend[cID] = True; 

def signup(pPacket, cID):
    Email = pPacket["Email"] # grab email and password from packet
    password = pPacket["password"]

    result = runDBCommand('''SELECT EXISTS(SELECT * from User WHERE Email = "%s")''' % Email) # check user is in database

    if(result[0] != 1): # if they're not

        passwordHash, salt = hash_password(password)
        runDBCommand('''INSERT INTO User (FullName, Email, password, salt) VALUES ("%s", "%s", "%s", "%s");''' % ("noName", Email, passwordHash, salt)) # add them with a noName variable to be updated later
    packetJSON[cID] = '{"pt" : "sr", "status" : 1}\n' # return status packet
    packetToSend[cID] = True; 

def login(pPacket, cID):
    global packetJSON
    global packetToSend

    Email = pPacket["Email"] # grab email and password from packet
    password = pPacket["password"]

    salt = runDBCommand('''SELECT salt from User WHERE Email = "%s"''' % Email)[0]
    storedPassword = runDBCommand('''SELECT password from User WHERE Email = "%s"''' % Email)[0]

    passwordHash = verify_password(storedPassword, password, salt);

    # result = runDBCommand('''SELECT EXISTS(SELECT * from User WHERE Email = "%s" AND password = "%s")''' % (Email, passwordHash)) #check if their user name and password is right
  
    sessionID = "";

    if (passwordHash): # if its correct 
        sessionID = uuid.uuid1() # generate sessionID as a UUID
        runDBCommand('''UPDATE User SET sessionID = "%s" WHERE email = "%s";''' % (str(sessionID), Email)) # add to database
        print("User", Email, "has logged in...")
    

    packetJSON[cID] = '{"pt" : "lr", "sessionID" : "%s"}\n' % sessionID # give them their statusID 
    packetToSend[cID] = True
           
def message(pPacket, cID):
    global tMessages

    ConvID = pPacket["convID"] # grab conversationID, messageBody, Email, sessionID and emotion from packet
    message = pPacket["message"]
    Email = pPacket["Email"]
    sessionID = pPacket["sessionID"]
    emotion = pPacket["emotion"]

    result = runDBCommand('''SELECT EXISTS(SELECT * from User WHERE Email = "%s" AND sessionID = "%s")''' % (Email, sessionID)) # check if user is logged in

    if(result[0]): # if user is logged in
         now = datetime.now() 
         userID = runDBCommand('''SELECT userID from User WHERE Email = "%s"''' % Email)[0] # get userID
         epoch = now.strftime('%Y-%m-%d %H:%M:%S') # get date and add message to database
         runDBCommand('''INSERT INTO Message (UserID, ConvID, MessageBody, CreateDate, emotion) values (%d, %d,"%s","%s","%s");''' % (userID, ConvID, message, epoch, emotion)) 
         tMessages += 1 # increment messages
         print(Email, ": ", message) # print to console

    else: # if user isnt logged in
        print(Email, "failed sessionID check.")

def clientSendThread(connection, cID):
   global packetToSend
   global packetJSON 
   global tMessages

   messageTotal = tMessages # set total message to equal the current number of messages sent
  
   while True:
      if packetToSend[cID] == True: # if a new message has been sent
         try:
            sendMessage = packetJSON[cID].encode() # encode the last sent message
            connection.send(sendMessage) # send the client the message
         except (ConnectionResetError, ConnectionAbortedError) as e:
             break # if the connection breaks

         packetToSend[cID] = False

      if tMessages != messageTotal: # if there is a new message to send

         lastMessageQuery = runDBCommand('''SELECT * FROM Message WHERE MessageID = %d;''' % (messageTotal + 1)) # grab newest message and variables
         lastMessage = lastMessageQuery[3]
         ConvID = lastMessageQuery[2]
         emotion = lastMessageQuery[5]
         lastMessageUsername = runDBCommand('''SELECT FullName FROM User WHERE UserID = %d;''' % (int(lastMessageQuery[1])))[0] 
         


         for i in range(len(packetToSend)):
            packetToSend[i] = True;
            packetJSON[i] = '{"pt" : "m", "username" : "' + lastMessageUsername + '", "message": "' + lastMessage + '", "ConvID": "' + str(ConvID) + '", "emotion": "' + emotion + '"}\n'

         messageTotal = tMessages # set messages so it doesnt re-loop

def clientThread(connection, address, cID):
   global tMessages

   sendThread = threading.Thread(target = clientSendThread, args = (connection, cID,)) # Create a thread for sending messages to the user
   sendThread.daemon = True; # daemon to true, thread will die when the main process does
   sendThread.start() # start

   while True:
      try:
         inPacket = connection.recv(1024) # wait for a message to be recieved

      except (ConnectionResetError, ConnectionAbortedError) as e: # if user closed client
         print(address[0] + " has disconnected.")
         break # leave loop

      inPacket = inPacket.decode('utf-8') # decode packet into string

      if inPacket != "":
         parsePacket(inPacket, cID) # parse this packet          
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

con = pooling.MySQLConnectionPool(**config) # create connection Pool to allow multiple users to update it.

packetToSend = []; # arrays with status' and packets to send, its janky but it works.
packetJSON = [];

clientID = 0 # ID of the next client to connect
clientRecvThreads = [] # Array of threads for clients.
tMessages = runDBCommand('''SELECT Count(*) FROM Message;''')[0]

print("Total messages in database: " + str(tMessages))

while True:
   sock.listen(1) # Server will wait here for a connection
   connection, address = sock.accept()

   print(address, " Has connected to the  server...")

   packetToSend.append(False)
   packetJSON.append("")

   clientRecvThreads.append(threading.Thread(target = clientThread, args = (connection, address, clientID,))) # Create a thread for the user's session
   clientRecvThreads[clientID].daemon = True; # daemon to true, thread will die when the main process does
   clientRecvThreads[clientID].start() # start thread

   clientID += 1
