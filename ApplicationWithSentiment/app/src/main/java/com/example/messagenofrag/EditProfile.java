package com.example.messagenofrag;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EditProfile extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 123;

    ImageView UserIcon;
    Button Btn_Change_Picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ConfigureBackBtn();

        ImageView img = findViewById(R.id.UserIcon);
            Button btn = findViewById(R.id.Btn_Change_Picture);
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
                finish();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            Uri imageData = data.getData();

            UserIcon.setImageURI(imageData);
            }
        }

    }