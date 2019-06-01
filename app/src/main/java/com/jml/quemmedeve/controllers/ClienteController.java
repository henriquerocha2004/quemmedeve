package com.jml.quemmedeve.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jml.quemmedeve.database.DebtorsDbHelper;

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



}
