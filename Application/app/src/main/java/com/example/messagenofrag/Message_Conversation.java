package com.example.messagenofrag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Message_Conversation extends AppCompatActivity {
    private RecyclerView mUsers_RecyclerView;
    private List<User> mUsers;
    private UsersAdapter mUsersAdapter;
    private int mPos;
    connectionThread connection = new connectionThread("androidAPP","52.142.184.189",1200);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__conversation);
        mUsers_RecyclerView = findViewById(R.id.users_recyclerview);
        mUsers_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsers = UsersBase.get().getUsers();
        mUsersAdapter = new UsersAdapter(mUsers);
        mUsers_RecyclerView.setAdapter(mUsersAdapter);
        EditText mEdit = (EditText) findViewById((R.id.theEditText));
        configureBackBtn();
        configureSend(mEdit);

        connection.start();
    }
    public static void SendMsg(String recievedMessage){
        User user = new User();
        user.setmMessage(recievedMessage);
        Log.d("Message Added:", recievedMessage);
        user.setmEmotion("happy");
        UsersBase.get().newMessage(user);
    }
    class UsersAdapter extends RecyclerView.Adapter<UserViewHolder>{
        private List<User> mUsers;
        public UsersAdapter(List<User> users) {
            super();
            this.mUsers = users;
        }


        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UserViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            holder.bind(this.mUsers.get(position), position);
        }

        @Override
        public int getItemCount() {
            return this.mUsers.size();
        }
    }
    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private UUID mID;
        private TextView mMessage;
        private TextView mEmotion;
        private int Pos;

        public UserViewHolder(ViewGroup container){
            super(LayoutInflater.from(Message_Conversation.this).inflate(R.layout.user_list_item, container, false));
            itemView.setOnClickListener(this);
            mMessage = (TextView) itemView.findViewById(R.id.User_msg);
            mEmotion = (TextView) itemView.findViewById(R.id.User_emotion);
        }
        private void bind(User user, int pos){
            Pos = pos;
            this.mID = user.getmID();
            mEmotion.setText(user.getmEmotion());
            mMessage.setText(user.getmMessage());
        }

        @Override
        public void onClick(View view) {
            mPos = Pos;
            Intent data = new Intent(Message_Conversation.this,Profile.class);
            data.putExtra("user_id",this.mID);
             startActivityForResult(data,1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                if(data!=null){
                    String action = data.getStringExtra("action");
                    if(action.equals("update")){
                        mUsersAdapter.notifyItemChanged(mPos);
                    }
                    else if(action.equals("delete")){
                        mUsersAdapter.notifyItemRemoved(mPos);
                    }
                }
            }
        }
    }
    private void configureBackBtn(){
        Button button = (Button) findViewById(R.id.btn_bck);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        Log.d("onDestory","called");
        connection.endConnection();
    }


    private void configureSend(EditText mEdit){
        Button button = (Button) findViewById(R.id.btn_send);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //mEdit.getText().toString() is the value held within the EditText box
                //Find a way to make the onClick method (This one) send the contents of mEdit to the database.
                String message = mEdit.getText().toString();

                messageObject.updateMessage(message);
                connection.sendMessage(message);
                mEdit.setText("", TextView.BufferType.EDITABLE);

            }
        });
    }
    public static void NewMessage(String Message){


    }
}
class User{
    private UUID mID;
    private String mMessage;
    private String mEmotion;

    public UUID getmID() {
        return mID;
    }
public User(){
        mID = UUID.randomUUID();
}
    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public void setmEmotion(String mEmotion) {
        this.mEmotion = mEmotion;
    }

    public String getmMessage() {
        return mMessage;
    }

    public String getmEmotion() {
        return mEmotion;
    }


}
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
    Boolean socketStatus;
    Socket s;

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
    }

    public void run()
    {
        try // look i just wanna check for IOException from socket but ofc buffered reader needs a try catch
        // as well and OFC they dont work if they're not in the same scope, it is jus easier this way okay??
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
                    break;
                }

                Log.d("Message Recieved:", recievedMessage);
                TheMessage = recievedMessage;
                Message_Conversation.SendMsg(TheMessage);
                lastRecievedMessage = this.recievedMessage;
            }

        }
        catch(Exception e)
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
            e.printStackTrace(); // idc fuck off
        }
    }
    public void sendMessage(String message)
    {
        this.message = message;
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

        this.sendWriter.write(encodeMessage(this.username));
        this.sendWriter.flush();

        while(true)
        {
            if (this.lastSentMessage != messageObject.returnMessage())
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
            String joinedString = String.join(" ", message);
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