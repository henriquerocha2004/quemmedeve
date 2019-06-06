package com.jml.quemmedeve.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.jml.quemmedeve.database.DebtsDbHelper;
import com.jml.quemmedeve.database.PaymentDbHelper;
import com.jml.quemmedeve.ultility.DateUltility;

import java.text.ParseException;


public class DebtController {


    public boolean store(ContentValues debt, ContentValues payment,  Context context){
        DebtsDbHelper debts = new DebtsDbHelper(context);
        SQLiteDatabase db = debts.getWritableDatabase();
        db.beginTransaction();

        try{
            long idDebt = db.insert(DebtsDbHelper.TABLE_NAME, null, debt);
            int parcelas = Integer.parseInt(debt.get(DebtsDbHelper.COLUMN_DEBT_SPLIT).toString());

            for(int i = 1; i <= parcelas ; i++){

                if(i > 1){
                    String dataPagamento = payment.get(PaymentDbHelper.COLUMN_PAYDAY).toString();
                    String Data = DateUltility.gerarProximaDataDePagamento(dataPagamento);
                    payment.put(PaymentDbHelper.COLUMN_PAYDAY, Data);
                }

                payment.put(PaymentDbHelper.COLUMN_DEBT_ID, idDebt);
                db.insert(PaymentDbHelper.TABLE_NAME, null, payment);
            }

           db.setTransactionSuccessful();
           return true;

        }catch (SQLException | ParseException e){
            Log.i("Erro", e.getMessage());
        }  finally {
            db.endTransaction();
        }
        return false;
    }

}
