package com.jml.quemmedeve.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + UserContract.User.TABLE_NAME + " (" +
                    UserContract.User.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    UserContract.User.COLUMN_NAME + " TEXT(100) NOT NULL," +
                    UserContract.User.COLUMN_PHONE + " TEXT(20) NOT NULL, "+
                    UserContract.User.COLUMN_LOGIN + " TEXT(50) NOT NULL, "+
                    UserContract.User.COLUMN_PASSWORD + " TEXT(50) NOT NULL)";

    private static final String SQL_DELETE_USERS = "DROP TABLE IF EXISTS " + UserContract.User.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "qmd.db";

    public UserDbHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USERS);
    }


}
