package com.example.messagenofrag;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {
    static TextView TV;
    String UN;
    static ImageView IV;
    Uri PPcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ConfigureBackBtn();
        ConfigureProfileBtn();
        reportBug();
        deleteProfile();

        PPcode = EditProfile.getPP();
        IV = findViewById(R.id.UserIcon);
        if (PPcode == null){

        }else{
            setPP(PPcode);
        }

        UN = EditProfile.getUN();
        TV = findViewById(R.id.UserName2);
        if(UN.equals("NOTHING YET")){

        }else{
            setName(UN);
        }

        Button emo = (Button) findViewById(R.id.btn_Avg_Emotion);
        emo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, Pop.class));
            }
        });
    }

    private void ConfigureBackBtn() {
        Button btn = findViewById(R.id.btn_Back_From_Profile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void reportBug(){
        Button TheButton = findViewById(R.id.btn_Report_Bug);
        TheButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "joshhunter6363@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Bug Report");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }

    public static void setName(String UN){
        TV.setText(UN);
    }

    public static void setPP(Uri PPcode){
        IV.setImageURI(null);
        IV.setImageURI(PPcode);
    }

    private void ConfigureProfileBtn() {
        Button theButton = findViewById(R.id.Btn_Edit_Profile);
        theButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this, EditProfile.class));
            }
        });
    }

    private void deleteProfile() {
        Button btn = findViewById(R.id.btn_Delete_Account);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete Profile!!!");
        alert.setMessage("ARE YOU SURE?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                connectionThread connection = MainActivity.sendConnection();
                String jsonToSend = "{\"pt\" : \"du\", \"Email\" : \"" + connection.username + "\", \"sessionID\" : \"" + connection.sessionID + "\"}";
                connection.sendJSON(jsonToSend);
            }
        });
        alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.create().show();
            }
        });
    }
}


