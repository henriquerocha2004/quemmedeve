package com.jml.quemmedeve.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {


    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_PASSWORD = "password";
    private static final String SQL_DELETE_USERS = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "qmd.db";

    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    COLUMN_NAME + " TEXT(100) NOT NULL," +
                    COLUMN_PHONE + " TEXT(20) NOT NULL, "+
                    COLUMN_LOGIN + " TEXT(50) NOT NULL, "+
                    COLUMN_PASSWORD + " TEXT(50) NOT NULL" +
            ")";

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

    public String getSQL_CREATE_USER(){
        return SQL_CREATE_USERS;
    }


}
