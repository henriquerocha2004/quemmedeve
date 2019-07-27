package com.jml.quemmedeve.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jml.quemmedeve.bean.DebtorsBean;
import com.jml.quemmedeve.bean.PaymentBean;
import com.jml.quemmedeve.database.DebtorsDbHelper;
import com.jml.quemmedeve.database.PaymentDbHelper;
import com.jml.quemmedeve.ultility.DateUltility;

public class PaymentController {

    public static String receivables(Context context){

        DebtorsDbHelper helper = new DebtorsDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor resultado = null;
        String valor = null;

        resultado = db.rawQuery("SELECT printf('%.2f', SUM(amount_to_pay)) as amount_to_pay FROM payment WHERE status_payment = 0 AND soft_delete = 0", null);

        if(resultado.getCount() > 0){
            resultado.moveToFirst();
            valor = resultado.getString(0);
        }

        return valor;
    }

    public static String receivablesMonth(Context context){
        DebtorsDbHelper helper = new DebtorsDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor resultado = null;
        String valor = null;
        ContentValues datas = DateUltility.getFistDataMonthLastDataMonth();

        resultado = db.rawQuery("SELECT printf('%.2f',SUM(amount_to_pay)) as previsão FROM payment " +
                "WHERE payday BETWEEN '"+datas.get("primeiraDataMes")+" 00:00:00' AND '"+datas.get("ultimaDataMes")+" 23:59:59' and status_payment = 0 AND soft_delete = 0", null);

        if(resultado.getCount() > 0){
            resultado.moveToFirst();
            valor = resultado.getString(0);
        }

        return valor;

    }


    public static boolean massPayment(Context context, int filterTypePayment, String mesAno, String idClient){

        String addConditionByMonth = "";
        PaymentDbHelper helper = new PaymentDbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] args = {idClient};

        db.beginTransaction();

        try{
            if(filterTypePayment == 1){
                addConditionByMonth = " AND payday BETWEEN '"+mesAno+"-01' AND '"+mesAno+"-31'";
            }

            db.execSQL("UPDATE payment SET status_payment = 1 WHERE debt_id IN (" +
                    "SELECT _id FROM debts WHERE usu_id_debt = ?  AND status_debt = 0 AND soft_delete = 0" +
                    ") AND soft_delete = 0 "+addConditionByMonth, args);
            db.setTransactionSuccessful();
            return true;

        }catch (Exception e){
            Log.i("Error: ", e.getMessage());
        }finally {
            db.endTransaction();
        }

        return false;
    }



}
