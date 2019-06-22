package com.jml.quemmedeve.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DebtorsDbHelper extends SQLiteOpenHelper {


    private Context context;
    public static final String DATABASE_NAME = "qmd.db";
    public static final String TABLE_NAME = "debtors";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SOFT_DELETE = "soft_delete";
    public static final int DATABASE_VERSION = 1;
    private static final String SQL_DELETE_DEBTORS = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String SQL_CREATE_DEBTORS =
       "CREATE TABLE " + TABLE_NAME+ " (" +
           COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
           COLUMN_NAME + " TEXT(100) NOT NULL," +
           COLUMN_PHONE + " NUMERIC(20) NOT NULL, " +
           COLUMN_SOFT_DELETE + " INTEGER NOT NULL DEFAULT(0)"+
         ")";

    private static final String SQL_CREATE_TRIGGER_MARK_DELETE_DEBTS =
            "CREATE TRIGGER mark_delete_debts\n" +
                    "         AFTER UPDATE OF soft_delete\n" +
                    "            ON debtors\n" +
                    "      FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "    UPDATE debts\n" +
                    "       SET soft_delete = 1\n" +
                    "     WHERE usu_id_debt = New._id;\n" +
                    "END;";

    public DebtorsDbHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
        context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DEBTORS);
        db.execSQL(SQL_CREATE_TRIGGER_MARK_DELETE_DEBTS);
        createOutherTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_DEBTORS);

    }

    public void createOutherTables(SQLiteDatabase db){

        DebtsDbHelper debtTable = new DebtsDbHelper(context);
        PaymentDbHelper payTable = new PaymentDbHelper(context);
        UserDbHelper userTable = new UserDbHelper(context);
        ReceptDBHelper receiptTable = new ReceptDBHelper(context);

        db.execSQL(debtTable.getSQL_CREATE_DEBTS());
        db.execSQL(debtTable.getSQL_CREATE_TRIGGER_MARK_DELETE_PAYMENTS());
        db.execSQL(payTable.getSQL_CREATE_PAYMENT());
        db.execSQL(payTable.getSQL_CREATE_TRIGGER_UPDATE_DEBT());
        db.execSQL(payTable.getSQL_CREATE_TRIGGER_MARK_DELETE_RECEIPT());
        db.execSQL(userTable.getSQL_CREATE_USER());
        db.execSQL(receiptTable.getSQL_CREATE_RECEIPT());

    }
}
