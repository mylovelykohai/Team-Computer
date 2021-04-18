package com.example.messagenofrag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;

import com.example.ContextGetter;

public class MainActivity extends AppCompatActivity {
    String ST;
    static TextView TV2;
    static MainActivity ma;
    static Context context;


    static connectionThread connection = new connectionThread("User","172.16.15.90",7777);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(getIntent());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configurePreviousMsg();
        reportBug();
        configureCreateProfile();
        ST = EditProfile.getST();
        TV2 = findViewById(R.id.textView22);

        ma = MainActivity.this; // allow static reference
        context = ContextGetter.getAppContext(); // context is stupid and everyone hates it

        if(ST.equals("NOTHING YET")){
        }
        else{
            setStatus(ST);
        }
        Button theButton = findViewById(R.id.TheMoveButton);
        connection.start();

        theButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, CreateAccount.class));
            }
        });
    }
    public static connectionThread sendConnection(){
        return connection;
    }
    private void reportBug(){
        Button TheButton = findViewById(R.id.ReportaBug2);
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

    public static void setStatus(String ST) {
        TV2.setText(ST);
    }

    public static void showToast(final String toast)
    {
        ma.runOnUiThread(new Runnable() { // this is all required because you can't run UI code on non-UI threads
            public void run() {
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show(); // toast for whatever reason
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