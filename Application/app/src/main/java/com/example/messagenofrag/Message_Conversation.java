package com.example.messagenofrag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Message_Conversation extends AppCompatActivity {
    private RecyclerView mUsers_RecyclerView;
    private List<User> mUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__conversation);
        mUsers_RecyclerView = findViewById(R.id.users_recyclerview);
        mUsers_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsers = UsersBase.get(getApplicationContext()).getUsers();
        mUsers_RecyclerView.setAdapter(new UsersAdapter(mUsers));
        EditText mEdit = (EditText) findViewById((R.id.theEditText));
        configureBackBtn();
        configureSend(mEdit);
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
            holder.bind(this.mUsers.get(position));
        }

        @Override
        public int getItemCount() {
            return this.mUsers.size();
        }
    }
    class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView mMessage;
        private TextView mEmotion;
        public UserViewHolder(ViewGroup container){
            super(LayoutInflater.from(Message_Conversation.this).inflate(R.layout.user_list_item, container, false));
            mMessage = (TextView) itemView.findViewById(R.id.User_msg);
            mEmotion = (TextView) itemView.findViewById(R.id.User_emotion);
        }
        private void bind(User user){
            mEmotion.setText(user.getmEmotion());
            mMessage.setText(user.getmMessage());
        }
    }
    private void configureBackBtn(){
        Button button = (Button) findViewById(R.id.btn_bck);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(Message_Conversation.this, MainActivity.class));
            }
        });
    }
    private void configureSend(EditText mEdit){
        Button button = (Button) findViewById(R.id.btn_send);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("EditText ", mEdit.getText().toString());
                //mEdit.getText().toString() is the value held within the EditText box
                //Find a way to make the onClick method (This one) send the contents of mEdit to the database.
            }
        });
    }
}
class User{
    private String mMessage;
    private String mEmotion;

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
