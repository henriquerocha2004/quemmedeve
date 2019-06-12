package com.jml.quemmedeve.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jml.quemmedeve.database.DebtorsDbHelper;
import com.jml.quemmedeve.database.DebtsDbHelper;

public class ClienteController {




    //Função que armazena os dados do cliente
    public static long store(String nome, String telefone, Context context){

        DebtorsDbHelper debtorsHelper = new DebtorsDbHelper(context);
        SQLiteDatabase db = debtorsHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put(DebtorsDbHelper.COLUMN_NAME, nome);
        valores.put(DebtorsDbHelper.COLUMN_PHONE, telefone);

        long idRow = db.insert(DebtorsDbHelper.TABLE_NAME, null, valores);
        return idRow;
    }


    public static Cursor findById(String id, Context context){

       Cursor cursor = null;
       DebtorsDbHelper helper = new DebtorsDbHelper(context);
       SQLiteDatabase db = helper.getReadableDatabase();

       try {

           String[] campos = {DebtorsDbHelper.COLUMN_NAME};
           String condicao = DebtorsDbHelper.COLUMN_ID + " = ?";
           String[] args = {id};
           cursor = db.query(DebtorsDbHelper.TABLE_NAME, campos,condicao,args,null,null,null);

           if(cursor != null){
               cursor.moveToFirst();
           }

       }catch (SQLException e){
           Log.i("Erro: ", e.getMessage());
       }finally {
           db.close();
       }

        return cursor;
    }

    public static Cursor getDebtsClient(String id, Context context){

        DebtorsDbHelper helper = new DebtorsDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = null;

        try {

         String[] arg = {id};
         result = db.rawQuery("SELECT debts._id, debts.debt_desc , printf('%.2f', debts.value) as value, strftime('%d-%m-%Y', debts.date_debt) as date_debt , debts.debt_split" +
                                    " FROM debtors" +
                                    " INNER JOIN debts ON debts.usu_id_debt = debtors._id" +
                                    " WHERE debtors._id = ? AND debts.status_debt = 0", arg);

         if(result != null){
             result.moveToFirst();
         }

        }catch (SQLException e){
            Log.i("Erro: ", e.getMessage());
        }finally {
            db.close();
        }

        return result;
    }

    public static Cursor getAllClientsList(Context context){

        DebtorsDbHelper helper = new DebtorsDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = null;

        try {
            result = db.rawQuery("SELECT distinct(debtors._id) as _id , debtors.name as name, printf('%.2f',debts.value) as total FROM debtors \n" +
                                      "INNER JOIN debts ON debts.usu_id_debt = debtors._id AND debts.status_debt = 0 GROUP BY debtors._id ORDER BY debtors.name ASC", null);

            if(result != null){
                result.moveToFirst();
            }

        }catch (SQLException e){
            Log.i("Erro: ", e.getMessage());
        }finally {
            db.close();
        }

        return result;
    }




}
