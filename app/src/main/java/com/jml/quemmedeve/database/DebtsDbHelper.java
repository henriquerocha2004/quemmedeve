package com.jml.quemmedeve.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DebtsDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_DEBTS =
            "CREATE TABLE " + DebtsContract.Debts.TABLE_NAME + " (" +
                    DebtsContract.Debts.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    DebtsContract.Debts.COLUMN_USU_ID + " INTEGER NOT NULL REFERENCES debtors(id)," +
                    DebtsContract.Debts.COLUMN_DEBT_DESC + " TEXT(200) NOT NULL, "+
                    DebtsContract.Debts.COLUMN_VALUE + " TEXT(50) NOT NULL, "+
                    DebtsContract.Debts.COLUMN_DATE_DEBT + " DATE NOT NULL," +
                    DebtsContract.Debts.COLUMN_DEBT_SPLIT + " INTEGER NOT NULL," +
                    DebtsContract.Debts.COLUMN_VALUE_SPLIT +" DOUBLE)";

    private static final String SQL_DELETE_DEBTS = "DROP TABLE IF EXISTS " + DebtsContract.Debts.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "qmd.db";

    public DebtsDbHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DEBTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_DEBTS);
    }


}
