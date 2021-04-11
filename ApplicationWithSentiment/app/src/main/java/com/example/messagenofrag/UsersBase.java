package com.example.messagenofrag;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.os.Build.ID;

public class UsersBase {
    private List<User> mUsers;
    private SQLiteDatabase mDatabase;
    private UsersBase(){
        mUsers = new ArrayList<>();
    }
    public List<User> getUsers(){
        return mUsers;
    }
    private static UsersBase mUserBase;
    public static UsersBase get(){
        if(mUserBase == null){
            mUserBase = new UsersBase();
        }
        return mUserBase;
    }
    public void deleteMessage(UUID ID){
        for(User user: mUsers){
            if(user.getmID().equals(ID)){
                mUsers.remove(user);

                break;
            }
        }
    }
    public void updateUser(UUID ID, String Message, String Emotion){
        for(User user:mUsers){
            if(user.getmID().equals(ID)){
                user.setmMessage(Message);
                user.setmEmotion(Emotion);
                break;
            }
        }
}
public User getUser(UUID id){
    for(User user:mUsers){
        if(user.getmID().equals(id)){
            return user;
        }
    }
    return null;
}
public void newMessage(User user){
        mUsers.add(user);
}
}

