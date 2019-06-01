package com.jml.quemmedeve.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DebtsDbHelper extends SQLiteOpenHelper {


    public static final String TABLE_NAME = "debts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USU_ID = "usu_id_debt";
    public static final String COLUMN_DEBT_DESC = "debt_desc";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_DATE_DEBT = "date_debt";
    public static final String COLUMN_DEBT_SPLIT = "debt_split";
    public static final String COLUMN_VALUE_SPLIT = "value_split";
    private static final String SQL_DELETE_DEBTS = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "qmd.db";

    private static final String SQL_CREATE_DEBTS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    COLUMN_USU_ID + " INTEGER NOT NULL REFERENCES debtors(id)," +
                    COLUMN_DEBT_DESC + " TEXT(200) NOT NULL, "+
                    COLUMN_VALUE + " TEXT(50) NOT NULL, "+
                    COLUMN_DATE_DEBT + " DATE NOT NULL," +
                    COLUMN_DEBT_SPLIT + " INTEGER NOT NULL," +
                    COLUMN_VALUE_SPLIT +" DOUBLE" +
              ")";

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
