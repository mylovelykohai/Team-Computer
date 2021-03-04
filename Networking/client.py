import socket
import sys 
import datetime 
import threading

def listen():
   while True:
      message = input() # ask user for input
      message = message.encode() # convert to something that can be sent
      sock.send(message) # send message

sock = socket.socket() # create socket

h_name = input(str("Enter the hostname of the server: ")) # User inputs the ip and username
u_name = input(str("Enter your username: "))
u_name = u_name.encode()

port = 8080 # default server port
 
sock.connect((h_name,port)) # connect to the serer

sock.send(u_name) # tell the server the username
print("Connected to chat server")
newPort = sock.recv(1024) # recieve the new port for the client's instance (eg. if this is the first client connected, give him 8081)
sock.close() # close the connection to port
print(newPort)

newPort = newPort.decode('utf-8') # convert to a readable format
newPort = int(newPort) # to int
sock = socket.socket() # create a new socket 

sock.connect((h_name,newPort)) # connect to new port

sendThread = threading.Thread(target=listen, args=()) # create thread for sending messages
sendThread.daemon = True; # close once script ends
sendThread.start() # start thread

while True: 
   incoming_message = sock.recv(1024) # recieve message 
   incoming_message = incoming_message.decode('utf-8') # convert to readable format
   fullTime = datetime.datetime.now()
   currTime = str(fullTime.day) + "/" + str(fullTime.month) + "/" + str(fullTime.year) + " " + str(fullTime.hour) + ":" + str(fullTime.minute)
   print("<" , currTime, ">", " ", incoming_message, sep='') # print the recieved message to console

