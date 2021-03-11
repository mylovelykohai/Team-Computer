package com.example.messagenofrag;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UsersBase {
    private List<User> mUsers;

    private SQLiteDatabase mDatabase;
    private UsersBase(Context context){

        mDatabase = new BackgroundWorker(context).getWritableDatabase();
        mUsers = new ArrayList<>();
        for(int i = 0; i<100; i++){
            User user = new User();
            user.setmEmotion("Emotion #"+i);
            user.setmMessage("Messages #"+i);
            mUsers.add(user);
        }
    }
    public List<User> getUsers(){
        return mUsers;
    }
    private static UsersBase mUserBase;
    public static UsersBase get(Context context){
        if(mUserBase == null){
            mUserBase = new UsersBase(context);
        }
        return mUserBase;
    }
}
