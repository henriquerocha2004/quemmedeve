package com.jml.quemmedeve.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PaymentDbHelper extends SQLiteOpenHelper {


    public static String TABLE_NAME = "payment";
    public static String COLUMN_ID = "_id";
    public static String COLUMN_DEBT_ID = "debt_id";
    public static String COLUMN_AMOUNT_PAY = "amount_to_pay";
    public static String COLUMN_PAYDAY = "payday";
    public static String COLUMN_STATUS_PAYMENT = "status_payment";
    private static final String SQL_DELETE_PAYMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "qmd.db";


    private static final String SQL_CREATE_PAYMENT =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    COLUMN_DEBT_ID + " INTEGER NOT NULL REFERENCES debts(_id)," +
                    COLUMN_AMOUNT_PAY + " DOUBLE NOT NULL, "+
                    COLUMN_PAYDAY + " DATE NOT NULL, "+
                    COLUMN_STATUS_PAYMENT + " BOOLEAN NOT NULL DEFAULT(0))";



    public PaymentDbHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PAYMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PAYMENT);
        onCreate(db);
    }

    public String getSQL_CREATE_PAYMENT(){
        return SQL_CREATE_PAYMENT;
    }


}
