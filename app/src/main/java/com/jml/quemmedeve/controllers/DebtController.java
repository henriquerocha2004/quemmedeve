package com.jml.quemmedeve.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jml.quemmedeve.database.DebtorsDbHelper;
import com.jml.quemmedeve.database.DebtsDbHelper;
import com.jml.quemmedeve.database.PaymentDbHelper;
import com.jml.quemmedeve.ultility.DateUltility;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class DebtController {

    public  List<String> datas = new ArrayList<String>();


    public boolean store(ContentValues debt, ContentValues payment,  Context context){
        DebtsDbHelper debts = new DebtsDbHelper(context);
        SQLiteDatabase db = debts.getWritableDatabase();
        db.beginTransaction();

        try{
            long idDebt = db.insert(DebtsDbHelper.TABLE_NAME, null, debt);
            int parcelas = Integer.parseInt(debt.get(DebtsDbHelper.COLUMN_DEBT_SPLIT).toString());

            datas.add(payment.get(PaymentDbHelper.COLUMN_PAYDAY).toString());

            for(int i = 1; i <= parcelas ; i++){

                if(i > 1){
                    String dataPagamento = payment.get(PaymentDbHelper.COLUMN_PAYDAY).toString();
                    String Data = DateUltility.gerarProximaDataDePagamento(dataPagamento);
                    datas.add(Data);
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

    public static Cursor getdebtAndPaymentById(String id, Context context){

        DebtsDbHelper helper = new DebtsDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = null;
        try {
            String[] arg = {id, id};
            result =  db.rawQuery("SELECT " +
                                "dbt."+DebtsDbHelper.COLUMN_DEBT_DESC+","+
                                " strftime('%d-%m-%Y', dbt."+DebtsDbHelper.COLUMN_DATE_DEBT+") date_debt, " +
                                " dbt."+DebtsDbHelper.COLUMN_VALUE+"," +
                                "dbt."+DebtsDbHelper.COLUMN_VALUE_SPLIT+", " +
                                "dbt."+DebtsDbHelper.COLUMN_DEBT_SPLIT+","+
                                "q.parc," +
                                "(printf('%.2f', dbt."+DebtsDbHelper.COLUMN_VALUE+" - (q.parc * dbt."+DebtsDbHelper.COLUMN_VALUE_SPLIT+"))) valor_restante " +
                            "FROM "+DebtsDbHelper.TABLE_NAME+" as dbt, " +
                                  "( SELECT COUNT(pay."+PaymentDbHelper.COLUMN_ID+") parc " +
                                  " FROM "+PaymentDbHelper.TABLE_NAME+" as pay " +
                                    " WHERE pay."+PaymentDbHelper.COLUMN_DEBT_ID+" = ?" +
                                            " AND pay."+PaymentDbHelper.COLUMN_STATUS_PAYMENT+" = 1 ) q" +
                                    " WHERE dbt."+DebtsDbHelper.COLUMN_ID+" = ?", arg);
           if(result != null){
               result.moveToFirst();
           }

        }catch (SQLException e){
            Log.i("Erro: ", e.getMessage());
        }

        return result;

    }

    public static Cursor getDebtForPayment(String id, Context context){

        PaymentDbHelper helper = new PaymentDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor result = null;

        try {
            String[] arg = {id};

            result = db.rawQuery("SELECT _id, printf('%.2f', amount_to_pay) as amount_to_pay, strftime('%d-%m-%Y', payday) as payday FROM payment WHERE debt_id = ? AND status_payment = 0", arg);

            if(result != null){
                result.moveToFirst();
            }

        }catch (SQLException e){
            Log.i("Erro: ", e.getMessage());
        }

        return result;

    }


    public static boolean makePayment(Context context, String id){

        PaymentDbHelper helper = new PaymentDbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("status_payment", 1);
        Cursor result = null;

        try {

            String[] args = {id};
            db.update(PaymentDbHelper.TABLE_NAME, valores, "_id = ?", args);

        }catch (SQLException e){
            Log.i("Erro: ", e.getMessage());
            return false;
        }

        return true;

    }

}
