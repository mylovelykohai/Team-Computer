package com.example.messagenofrag;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setBackButton();
        setSubmit();
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
    public void setSubmit(){
        EditText UserName = findViewById(R.id.editTextUsername);
        EditText Password = findViewById(R.id.editTextPassword);
        String UN = UserName.getText().toString();
        String PW = Password.getText().toString();
        Button button = findViewById(R.id.btnCreateAccount);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Submit the Username and Password to create account
                //pls do database connection here and do login <3
            }
        });
    }
}