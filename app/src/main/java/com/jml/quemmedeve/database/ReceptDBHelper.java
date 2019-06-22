package com.jml.quemmedeve.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReceptDBHelper extends SQLiteOpenHelper {

    public static String TABLE_NAME = "receipt";
    public static String COLUMN_ID = "_id";
    public static String COLUMN_REC_ID_PAY = "rec_id_pay";
    public static String COLUMN_RECEIPT = "receipt";
    public static String COLUMN_REC_ID_DEBTOR = "rec_id_debtor";
    public static String COLUMN_REC_ID_DEBT = "rec_id_debt";
    public static String COLUMN_SOFT_DELETE = "soft_delete";
    private static final String SQL_DELETE_RECEIPT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "qmd.db";

    private static final String SQL_CREATE_RECEIPT = "" +
            "CREATE TABLE "+TABLE_NAME+" (\n" +
            "    "+COLUMN_ID+"          INTEGER    PRIMARY KEY AUTOINCREMENT\n" +
            "                             NOT NULL,\n" +
            "    "+COLUMN_REC_ID_PAY+"    INT        NOT NULL\n" +
            "                             REFERENCES payment (id) ON DELETE CASCADE,\n" +
            "    "+COLUMN_RECEIPT+"        TEXT (500) NOT NULL,\n" +
            "    "+COLUMN_REC_ID_DEBTOR+"            REFERENCES debtors (id) ON DELETE CASCADE,\n" +
            "    "+COLUMN_REC_ID_DEBT+"   INT        REFERENCES debts (id) ON DELETE CASCADE,\n" +
            "    "+COLUMN_SOFT_DELETE+"   INT        NOT NULL DEFAULT(0)\n" +
            ")";


    public ReceptDBHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RECEIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_RECEIPT);
    }

    public String getSQL_CREATE_RECEIPT(){
        return SQL_CREATE_RECEIPT;
    }
}
