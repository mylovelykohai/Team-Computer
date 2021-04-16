package com.example.messagenofrag;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditProfile extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 123;

    ImageView UserIcon;
    Button Btn_Change_Picture;

    TextView txtstat;
    TextView username;

    EditText nameEditText;
    EditText statEditText;

    Button updateName;
    Button updateStatus;
    static String UN = "NOTHING YET";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ConfigureBackBtn();

        ImageView img = findViewById(R.id.UserIcon);
        Button btn = findViewById(R.id.Btn_Change_Picture);

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

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery
                Intent intent = new Intent();
                intent.setType("image/^");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Pick an image"), GALLERY_REQUEST_CODE);

                //ConfigureChangePic();

                //UserIcon = findViewById(R.id.UserIcon);
                //Btn_Change_Picture = findViewById(Btn_Change_Picture);
            }
        });
    }


        private void ConfigureBackBtn(){
            Button btn = findViewById(R.id.btn_Back_From_Edit);
            btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(EditProfile.this, UserProfile.class));
                }
            });
        }
        public static String getUN(){
            return UN;
        }


    //private void ConfigureChangePic(){
    //    ImageView img = findViewById(R.id.UserIcon);
    //    Button btn = findViewById(R.id.Btn_Change_Picture);
    //    btn.setOnClickListener(new View.OnClickListener() {
    //        @Override
    //        public void onClick(View v) {
    //            //open gallery
    //            Intent intent = new Intent();
    //            intent.setType("image/^");
    //            intent.setAction(Intent.ACTION_GET_CONTENT);
    //            startActivityForResult(Intent.createChooser(intent, "Pick an image", GALLERY_REQUEST_CODE));
    //        }
    //    });
    //}

        //protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        //if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
        //Uri imageData = data.getData();

        //UserIcon.setImageURI(imageData);
        //}
        //}
        //)


    }