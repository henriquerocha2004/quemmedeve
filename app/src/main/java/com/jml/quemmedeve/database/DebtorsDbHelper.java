package com.jml.quemmedeve.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DebtorsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "qmd.db";
    public static final String TABLE_NAME = "debtors";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ID = "id";
    public static final int DATABASE_VERSION = 1;
    private static final String SQL_DELETE_DEBTORS = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String SQL_CREATE_DEBTORS =
       "CREATE TABLE " + TABLE_NAME+ " (" +
           COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
           COLUMN_NAME + " TEXT(100) NOT NULL," +
           COLUMN_PHONE + " NUMERIC(20) NOT NULL " +
         ")";


    public DebtorsDbHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DEBTORS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_DEBTORS);
    }
}
