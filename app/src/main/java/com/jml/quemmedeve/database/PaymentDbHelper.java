package com.jml.quemmedeve.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PaymentDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_PAYMENT =
            "CREATE TABLE " + PaymentContract.Payment.TABLE_NAME + " (" +
                    PaymentContract.Payment.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    PaymentContract.Payment.COLUMN_DEBT_ID + " INTEGER NOT NULL REFERENCES debts(id)," +
                    PaymentContract.Payment.COLUMN_AMOUNT_PAY + " DOUBLE NOT NULL, "+
                    PaymentContract.Payment.COLUMN_PAYDAY + " DATE NOT NULL, "+
                    PaymentContract.Payment.COLUMN_STATUS_PAYMENT + " BOOLEAN NOT NULL DEFAULT(false))";

    private static final String SQL_DELETE_PAYMENT = "DROP TABLE IF EXISTS " + PaymentContract.Payment.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "qmd.db";

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
    }


}
