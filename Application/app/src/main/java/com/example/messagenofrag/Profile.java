package com.example.messagenofrag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

public class Profile extends AppCompatActivity {
    private EditText mMessage_ET;
    private EditText mEmotion_ET;
    private Button mBtn_back;
    private Button mBtn_del;
    private Button mBtn_update;
    private UUID mID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mID = (UUID) getIntent().getSerializableExtra("user_id");
        User user = (User) UsersBase.get().getUser(mID);

        mEmotion_ET = (EditText) findViewById(R.id.Emotion_ET);
        mMessage_ET = (EditText) findViewById(R.id.Message_ET);
        mEmotion_ET.setText(user.getmEmotion());
        mMessage_ET.setText(user.getmMessage());
        mBtn_back = (Button) findViewById(R.id.btn_bck1);
        mBtn_del = (Button) findViewById(R.id.btn_del);
        mBtn_update = (Button) findViewById(R.id.btn_update);
        mBtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBtn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("action","update");
                setResult(RESULT_OK,intent);
                UsersBase.get().updateUser(mID,mMessage_ET.getText().toString(),mEmotion_ET.getText().toString());
                Toast.makeText(Profile.this, "Message updated!.",Toast.LENGTH_LONG).show();
            }
        });
        mBtn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("action","delete");
                setResult(RESULT_OK,intent);
                UsersBase.get().deleteMessage(mID);
                Toast.makeText(Profile.this, "Message Deleted!.",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}