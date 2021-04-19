package com.example.messagenofrag;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ContextGetter;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setBackButton();
        setCreateAccount();
        setLogIn();
    }
    public void setBackButton(){
        Button THEBUTTON = findViewById(R.id.btnCreateAccBck);
        THEBUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void setCreateAccount(){
        EditText UserName = findViewById(R.id.editTextUsername);
        EditText Password = findViewById(R.id.editTextPassword);
        Button button = findViewById(R.id.btnCreateAccount);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                String UN = UserName.getText().toString();
                String PW = Password.getText().toString();
                connectionThread connection = MainActivity.sendConnection();
                String jsonToSend = "{\"pt\" : \"s\", \"Email\" : \"" + UN + "\", \"password\" : \"" + PW + "\"}";

                connection.sendJSON(jsonToSend);
                connection.username = UN;
            }
        });
    }
    public void setLogIn(){
        EditText UserName = findViewById(R.id.editTextUsername);
        EditText Password = findViewById(R.id.editTextPassword);
        Button button = findViewById(R.id.btnLogIn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String UN = UserName.getText().toString();
                String PW = Password.getText().toString();
                connectionThread connection = MainActivity.sendConnection();
                String jsonToSend = "{\"pt\" : \"l\", \"Email\" : \"" + UN + "\", \"password\" : \"" + PW + "\"}";

                connection.sendJSON(jsonToSend);
                connection.username = UN;
            }
        });
    }
}