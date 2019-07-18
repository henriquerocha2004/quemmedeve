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


    //Função que faz a consulta pelo ID do cliente
    public static Cursor findById(String id, Context context){

       Cursor cursor = null;
       DebtorsDbHelper helper = new DebtorsDbHelper(context);
       SQLiteDatabase db = helper.getReadableDatabase();

       try {

          String[] args = {id, id};

          cursor = db.rawQuery("SELECT debtors.name, debtors.phone, (\n" +
                  "    SELECT printf('%.2f', SUM(payment.amount_to_pay)) as valor_total FROM payment\n" +
                  "        INNER JOIN debts ON debts._id = payment.debt_id\n" +
                  "    WHERE debts.usu_id_debt = ? AND payment.status_payment = 0 AND payment.soft_delete = 0\n" +
                  ")as valor_total  \n" +
                  "FROM debtors\n" +
                  "WHERE debtors._id = ?", args);

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

    // Função que coleta todos os déditos do cliente
    public static List<DebtsBean> getDebtsClient(String id, Context context, boolean period, String monthYear){

        String conditionPeriod = "";

        if(period){
            conditionPeriod = " AND payment.payday BETWEEN '"+monthYear+"-01' AND '"+monthYear+"-31'";
        }

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
                       " INNER JOIN payment ON payment.debt_id = debts._id"+
                       " WHERE debts.usu_id_debt = ? AND debts.soft_delete = 0 "+conditionPeriod+" GROUP BY debts.debt_desc ORDER BY status_debt ASC", arg);
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
                 debtsList.add(debt);
             }while (debts.moveToNext());

         }

        }catch (SQLException e){
            Log.i("Erro: ", e.getMessage());
        }finally {
            debts.close();
            db.close();
        }

        return debtsList;
    }

    // Função que coleta todos os clientes com débitos a pagar
    public static List<DebtorsBean> getAllDebtors(Context context, boolean searchOrigin, String likeCondition){

        String conditionSearch = "";

        if(searchOrigin == true){
            conditionSearch = "AND debtors.name LIKE '%"+likeCondition+"%'";
        }

        DebtorsDbHelper helper = new DebtorsDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor debtors = null;
        List<DebtorsBean> listDebtors = new ArrayList<>();

        try {
            debtors = db.rawQuery("SELECT distinct(debtors._id) as _id , debtors.name as name, printf('%.2f',SUM(payment.amount_to_pay)) as total FROM debtors \n" +
                                      "INNER JOIN debts ON debts.usu_id_debt = debtors._id " +
                                      "INNER JOIN payment ON payment.debt_id = debts._id "+
                                      " WHERE payment.status_payment = 0 AND debts.soft_delete = 0 "+conditionSearch+" GROUP BY debtors._id ORDER BY debtors.name ASC", null);


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
            debtors.close();
            db.close();
        }

        return listDebtors;
    }

    // Função que coleta todos os clientes.
    public static List<DebtorsBean> getAllClients(Context context, boolean searchOrigin, String likeCondition){

        String conditionSearch = "";

        if(searchOrigin == true){
            conditionSearch = "AND debtors.name LIKE '%"+likeCondition+"%'";
        }


        DebtorsDbHelper helper = new DebtorsDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor clients = null;
        List<DebtorsBean> listClients = new ArrayList<>();

        try{
            clients = db.rawQuery("SELECT _id, name, phone as total FROM debtors WHERE soft_delete = 0 "+conditionSearch+" ORDER BY name ASC", null);

            if(clients.getCount() > 0){
                clients.moveToFirst();

                do{
                    DebtorsBean client = new DebtorsBean();
                    client.setId(clients.getInt(0));
                    client.setName(clients.getString(1));
                    client.setValueDebt(clients.getString(2));
                    listClients.add(client);
                }while (clients.moveToNext());

            }

        }catch (SQLException e){
            Log.i("Erro: ", e.getMessage());
        }
        return listClients;
    }

}
