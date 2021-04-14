package com.example.messagenofrag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(getIntent());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configurePreviousMsg();
        reportBug();
        configureCreateProfile();
        Button theButton = findViewById(R.id.TheMoveButton);
        theButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, CreateAccount.class));
            }
        });
    }

    private void reportBug(){
        Button button = findViewById(R.id.reportaBug2);
        button.setOnClickListener(new View.OnClickListener(){
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

    private void configurePreviousMsg(){
        //The purpose of this is to populate the "Button Contains Previous Message" list with actual content
        //Each button will need it's own SQL query
        //SELECT msg AS most_recent_message FROM messages WHERE Conversation_ID = conversation
        //for each button, is a suggestion, I'll leave this to the connection team!
        //This all happens on load, meaning the most recent message will only load when the page is refreshed
        //The page gets reset when any button is pressed, unfortunately, that is the only way I can do this
        int i = 1;
        Integer btnNo;
        Integer btnContactName;
        Button theButton;
        TextView TV2;
        while(i<10){
            btnNo = getResources().getIdentifier("Contact_"+i+"_Prevmgs","id",getPackageName());
            btnContactName = getResources().getIdentifier("Contact_"+i+"_name","id",getPackageName());
            theButton = (Button) findViewById(btnNo);
            TV2 = (TextView) findViewById(btnContactName);
            //theButton.setText("Message Currently un-coded!"); //FIND A WAY TO GRAB THE PREVIOUS MESSAGE OF CONVERSATION I HERE
            //@CONNECTION TEAM
            //TV2.setText("Name un-coded!"); //Same deal, grab the name of the contact
            //@Connection team
            theButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    startActivity(new Intent(MainActivity.this, Message_Conversation.class));
                    //CURRENTLY ALL BUTTONS TAKE YOU TO THE SAME PLACE
                    //AGAIN THIS SHOULD BE FIXED BY CONNECTION TEAM
                }
            });
            i = i+1;
        }
    }
    private void configureCreateProfile(){
        ImageView IV;
        IV = (ImageView) findViewById(R.id.imageView13);
        IV.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View view) {
              startActivity(new Intent(MainActivity.this, UserProfile.class));
          }
      });
    }
}