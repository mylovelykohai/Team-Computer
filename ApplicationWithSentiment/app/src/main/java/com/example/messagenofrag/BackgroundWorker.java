package com.example.messagenofrag;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BackgroundWorker extends SQLiteOpenHelper{
    public BackgroundWorker(@Nullable Context context) {
        super(context, "test.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users("+
                "_id integer primary key autoincrement, "+
                "_uuid,"+
                "username,"+
                "message)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
