package com.jml.quemmedeve.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jml.quemmedeve.bean.DebtorsBean;
import com.jml.quemmedeve.bean.DebtsBean;
import com.jml.quemmedeve.database.DebtorsDbHelper;
import com.jml.quemmedeve.ultility.NumberUtility;

import java.util.ArrayList;
import java.util.List;

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

          String[] args = {id};

          cursor = db.rawQuery("SELECT debtors.name, debtors.phone, printf('%.2f', SUM(payment.amount_to_pay)) as valor_total FROM debtors" +
                  " INNER JOIN debts ON debts.usu_id_debt = debtors._id" +
                  " INNER JOIN payment ON payment.debt_id = debts._id " +
                  "WHERE debtors._id = ? AND payment.status_payment = 0 AND payment.soft_delete = 0", args);

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

    public static List<DebtsBean> getDebtsClient(String id, Context context){

        DebtorsDbHelper helper = new DebtorsDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor debts = null;
        List<DebtsBean> debtsList = new ArrayList<>();
        try {

         String[] arg = {id};
            debts = db.rawQuery(
                  "SELECT debts._id, debts.debt_desc , printf('%.2f', debts.value) as value, printf('%.2f', debts.value_split) as value_split , debts.debt_split, " +
                      "debts.status_debt" +
                      " FROM debts" +
                      " WHERE debts.usu_id_debt = ? AND debts.soft_delete = 0 ORDER BY status_debt DESC", arg);


         System.out.println(debts.getCount());

         if(debts.getCount() > 0){
             debts.moveToFirst();

             do{
                 DebtsBean debt = new DebtsBean();
                 debt.setId(debts.getInt(0));
                 debt.setDebt_desc(debts.getString(1));
                 debt.setValue(debts.getString(2));
                 debt.setValue_split(debts.getString(3));
                 debt.setDebt_split(debts.getString(4));
                 debt.setStatus_debt(debts.getInt(5));

                 System.out.println(debt.toString());


                 debtsList.add(debt);
             }while (debts.moveToNext());

         }

        }catch (SQLException e){
            Log.i("Erro: ", e.getMessage());
        }finally {
            db.close();
        }

        return debtsList;
    }

    public static List<DebtorsBean> getAllClientsList(Context context){

        DebtorsDbHelper helper = new DebtorsDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor debtors = null;
        List<DebtorsBean> listDebtors = new ArrayList<>();

        try {
            debtors = db.rawQuery("SELECT distinct(debtors._id) as _id , debtors.name as name, printf('%.2f',SUM(payment.amount_to_pay)) as total FROM debtors \n" +
                                      "INNER JOIN debts ON debts.usu_id_debt = debtors._id " +
                                      "INNER JOIN payment ON payment.debt_id = debts._id "+
                                      " WHERE payment.status_payment = 0 AND debts.soft_delete = 0 GROUP BY debtors._id ORDER BY debtors.name ASC", null);


            if(debtors.getCount() > 0){
                debtors.moveToFirst();

                do{
                    DebtorsBean debtor = new DebtorsBean();
                    debtor.setId(debtors.getInt(0));
                    debtor.setName(debtors.getString(1));
                    debtor.setValueDebt(NumberUtility.converterBr(debtors.getString(2)));
                    listDebtors.add(debtor);
                }while(debtors.moveToNext());
            }

        }catch (SQLException e){
            Log.i("Erro: ", e.getMessage());
        }finally {
            db.close();
        }

        return listDebtors;
    }
}
