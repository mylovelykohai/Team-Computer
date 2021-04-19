package com.example.messagenofrag;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


class connectionThread extends Thread
{
    int PORT;
    String IPADDR;

    String username;

    String message;
    String recievedMessage;
    String lastSentMessage;
    String lastRecievedMessage;
    static String TheMessage;
    String sessionID;
    Boolean socketStatus;
    Socket s;

    JSONArray JSON = new JSONArray();
    JSONObject JSONobject = new JSONObject();
    private messageObject messageClass;

    connectionThread(String username, String IPADDR, int PORT)
    {
        this.username = username;
        this.IPADDR = IPADDR;
        this.PORT = PORT;
        this.message = "";
        this.recievedMessage = "";
        this.lastSentMessage = "";
        this.lastRecievedMessage = "";
        this.messageClass = messageClass;
        this.sessionID = "a";
        this.socketStatus = true;
    }

    public void run()
    {
        try // look i just wanna check for IOException from socket but ofc buffered reader needs a try catch
        // as well and ofc they don't work if they're not in the same scope, it is just easier this way okay??
        {
            this.s = new Socket();
            this.s.connect(new InetSocketAddress(IPADDR, PORT));
            BufferedReader recvReader = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            connectionSendThread sendThread = new connectionSendThread(this.s, username, messageClass);
            sendThread.start();

            while (true)
            {
                try
                {
                    this.recievedMessage = recvReader.readLine();
                }
                catch(SocketException e)
                {
                    Log.d("e","SocketException, exiting recv loop");
                    MainActivity.showToast("Disconnected from server");
                    break;
                }

                parsePacket(this.recievedMessage);

                lastRecievedMessage = this.recievedMessage;
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void parsePacket(String recievedMessage)
    {

        try
        {
            JSONobject = new JSONObject(recievedMessage);
            switch (JSONobject.getString("pt"))
            {
                case "lr": // Check if the login has been successful or not, then update sessionID with the current sessionID.
                    if (JSONobject.getString("sessionID").length() == 36)
                    {
                        MainActivity.showToast("Logged in!");
                        sessionID = JSONobject.getString("sessionID");
                    }
                    else
                    {
                        MainActivity.showToast("Login failed");
                    }
                    break;
                case "m":
                    String ConvID = JSONobject.getString("ConvID");
                    String emotion = JSONobject.getString("emotion");
                    String displayMessage = JSONobject.getString("username") + ": " + JSONobject.getString("message");
                    Message_Conversation.SendMsg(displayMessage, emotion);
                    break;
                case "dr":
                    int deleteStatus = JSONobject.getInt("status");
                    if(deleteStatus == 1)
                    {
                        MainActivity.showToast("Account OBLITERATED!");
                    }
                    else
                    {
                        MainActivity.showToast("Account not deleted :(");
                    }
                case "sr":
                    int signUpStatus = JSONobject.getInt("status");
                    if(signUpStatus == 1)
                    {
                        MainActivity.showToast("Account Created!");
                    }
                    else
                    {
                        MainActivity.showToast("Account not created :(");
                    }
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
    public void endConnection() {
        try
        {
            this.s.close();
            this.recievedMessage = "";
            messageObject.updateMessage("");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void sendJSON(String message)
    {
        messageObject.updateMessage(message);
    }
    public void sendMessage(String message, int convID, String emotion)
    {
        this.message = "{\"pt\": \"m\", \"Email\" : \"" + username + "\", \"message\" : \"" + message +"\", \"convID\" : " + convID + ", \"sessionID\" : \"" + sessionID + "\", \"emotion\" : \"" + emotion + "\"}";
        messageObject.updateMessage(this.message);
    }

    protected String encodeMessage(String... message)
    {
        try
        {
            String encodedString = String.join(" ", message);
            encodedString = URLEncoder.encode(encodedString, "UTF-8");
            return encodedString;
        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }
}

class connectionSendThread extends Thread
{
    Socket s;
    String username;
    String message;
    String lastSentMessage;
    PrintWriter sendWriter;
    messageObject messageClass;

    connectionSendThread(Socket _Socket, String username, messageObject messageClass)
    {
        this.s = _Socket;
        this.username = username;
        this.message = message;
        this.messageClass = messageClass;
    }

    public void run()
    {
        try
        {
            this.sendWriter = new PrintWriter(s.getOutputStream());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        while(true)
        {
            if (this.lastSentMessage != messageObject.returnMessage() && messageObject.returnMessage() != "")
            {
                this.sendWriter.write(encodeMessage(messageObject.returnMessage()));
                this.lastSentMessage = messageObject.returnMessage();
                this.sendWriter.flush();
            }
        }
    }
    protected String encodeMessage(String... message)
    {
        try
        {
            String joinedString = null;
            joinedString = String.join(" ", message);

            String encoded = URLEncoder.encode(joinedString, "UTF-8");
            encoded = encoded.replace('+', ' ');
            return encoded;
        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }

}

class messageObject
{
    public static volatile String message;

    public static void updateMessage(String newMessage)
    {
        message = newMessage;
    }

    public static String returnMessage()
    {
        return message;
    }
}