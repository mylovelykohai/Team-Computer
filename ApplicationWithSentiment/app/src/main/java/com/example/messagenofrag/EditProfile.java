package com.example.messagenofrag;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    private static final int imgPickCode = 100;
    private static final int permCode = 101;

    ImageView UserIcon;
    Button Btn_Change_Picture;
    Uri imageUri;

    TextView txtstat;
    TextView username;

    EditText nameEditText;
    EditText statEditText;

    Button updateName;
    Button updateStatus;

    static String UN = "NOTHING YET";
    static String ST = "NOTHING YET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ConfigureBackBtn();

        ImageView img = findViewById(R.id.UserIcon);
        Button btnPic = findViewById(R.id.Btn_Change_Picture);

        EditText nameEditText = findViewById(R.id.editTextUsername);
        EditText statEditText = findViewById(R.id.editTextStatus);

        Button updateName = findViewById(R.id.Btn_Change_Username);
        Button updateStatus = findViewById(R.id.Btn_Change_Status);

        TextView textName = findViewById(R.id.UserName2);
        TextView textStatus = findViewById(R.id.textView22);

        updateName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                textName.setText(name);
                UN = name;
                UserProfile.setName(UN);
            }
        });

        updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = statEditText.getText().toString();
                ST = status;
                MainActivity.setStatus(ST);
            }
        });

        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/^");

                startActivityForResult(Intent.createChooser(gallery, "Select picture"), imgPickCode);
            }
        });

    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == imgPickCode && resultCode == RESULT_OK && data != null){
                imageUri = data.getData();
                UserIcon.setImageURI(imageUri);

                }
            }






        private void ConfigureBackBtn(){
            Button btn = findViewById(R.id.btn_Back_From_Edit);
            btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    UserProfile.setName(UN);
                    finish();
                }
            });
        }


        public static String getUN(){
            return UN;
        }
        public static String getST(){
            return ST;
        }


    }