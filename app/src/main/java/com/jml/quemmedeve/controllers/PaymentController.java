package com.jml.quemmedeve.controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jml.quemmedeve.database.DebtorsDbHelper;

public class PaymentController {

    public static String receivables(Context context){

        DebtorsDbHelper helper = new DebtorsDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor resultado = null;
        String valor = null;

        resultado = db.rawQuery("SELECT printf('%.2f', SUM(amount_to_pay)) as amount_to_pay FROM payment WHERE status_payment = 0 AND soft_delete = 0", null);

        System.out.println(resultado.getCount());

        if(resultado.getCount() > 0){
            resultado.moveToFirst();
            valor = resultado.getString(0);
        }

        return valor;
    }
}
