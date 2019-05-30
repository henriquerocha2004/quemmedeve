package com.jml.quemmedeve.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DebtorsDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = "TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_DEBTORS =
       "CREATE TABLE " + DebtorsContract.Debtors.TABLE_NAME + " (" +
       DebtorsContract.Debtors.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
       DebtorsContract.Debtors.COLUMN_NAME + " TEXT(100) NOT NULL," +
       DebtorsContract.Debtors.COLUMN_PHONE + " NUMERIC(20) NOT NULL )";

    private static final String SQL_DELETE_DEBTORS = "DROP TABLE IF EXISTS " + DebtorsContract.Debtors.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "qmd.db";

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
