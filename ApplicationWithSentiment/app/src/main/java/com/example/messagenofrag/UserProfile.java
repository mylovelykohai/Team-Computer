package com.example.messagenofrag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {
    static TextView TV;
String UN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ConfigureBackBtn();
        ConfigureProfileBtn();
        reportBug();
        UN = EditProfile.getUN();
        TV = findViewById(R.id.UserName2);
        if(UN.equals("NOTHING YET")){

        }
        else{
            setName(UN);
        }
        //ImageView UserIcon = (ImageView) findViewById(R.id.UserIcon);
        //UserIcon.setImageResource(R.drawable.fpscan);

        //UserIcon.setImageResource(R.drawable.monkey);

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
    private void ConfigureProfileBtn() {
        Button theButton = findViewById(R.id.Btn_Edit_Profile);
        theButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this, EditProfile.class));
            }
        });
    }
}