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
        DebtsDbHelper debt = new DebtsDbHelper(context);
        SQLiteDatabase db = debt.getReadableDatabase();
        db = helper.getReadableDatabase();
        Cursor result = null;

        try {

         String[] arg = {id};
         result = db.rawQuery("SELECT debts.debt_desc ,debts.value,debts.date_debt, debts.debt_split" +
                                    " FROM debtors" +
                                    " INNER JOIN debts ON debts.usu_id_debt = debtors.id" +
                                    " WHERE debtors.id = ? AND debts.status_debt = 0", arg);
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
