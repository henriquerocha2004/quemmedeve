package com.jml.quemmedeve.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jml.quemmedeve.bean.DebtsBean;
import com.jml.quemmedeve.database.DebtorsDbHelper;
import com.jml.quemmedeve.database.DebtsDbHelper;
import com.jml.quemmedeve.database.PaymentDbHelper;
import com.jml.quemmedeve.ultility.DateUltility;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class DebtController {

    public  List<String> datas = new ArrayList<String>();

    // Função que salva um débito de um cliente
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

    // Função que localiza um débito pelo ID
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
                                            " AND pay."+PaymentDbHelper.COLUMN_STATUS_PAYMENT+" = 1 AND pay.soft_delete = 0) q" +
                                    " WHERE dbt."+DebtsDbHelper.COLUMN_ID+" = ? AND dbt.soft_delete = 0", arg);
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

            result = db.rawQuery("SELECT _id, printf('%.2f', amount_to_pay) as amount_to_pay, strftime('%d-%m-%Y', payday) as payday FROM payment WHERE debt_id = ? AND status_payment = 0 AND soft_delete = 0", arg);

            if(result != null){
                result.moveToFirst();
            }

        }catch (SQLException e){
            Log.i("Erro: ", e.getMessage());
        }

        return result;
    }

    public static List<DebtsBean> getDebtsForDebtor(String idDebtor, int typeQuery ,Context context){

        String addDataCondition = "";
        List<DebtsBean> debts = new ArrayList<>();
        if(typeQuery == 0){
            ContentValues dates = DateUltility.getFistDataMonthLastDataMonth();
            addDataCondition = "AND date_debt BETWEEN '"+ dates.get("primeiraDataMes") + "' AND '" + dates.get("ultimaDataMes")+ "'";
        }

        System.out.println(addDataCondition);

      try{

          DebtsDbHelper helper = new DebtsDbHelper(context);
          SQLiteDatabase db = helper.getReadableDatabase();
          String[] args = {idDebtor};
          Cursor resultSet = db.rawQuery(
                  "SELECT debts._id, debts.debt_desc, printf('%.2f',debts.value) as full_value, date_debt, printf('%.2f',payment.amount_to_pay) as amount_to_pay , printf('%.2f', SUM(payment.amount_to_pay)) as remaining_value\n" +
                          "FROM debts \n" +
                          "   INNER JOIN payment ON payment.debt_id = debts._id\n" +
                          "WHERE debts.usu_id_debt = ? AND debts.soft_delete = 0 AND payment.status_payment = 0 AND payment.soft_delete = 0 " + addDataCondition + " GROUP BY payment.amount_to_pay ORDER BY debts.date_debt ASC", args);

          if(resultSet != null){
              resultSet.moveToFirst();

              do{
                  DebtsBean debt = new DebtsBean();
                  debt.setId(resultSet.getInt(0));
                  debt.setDebt_desc(resultSet.getString(1));
                  debt.setValue(resultSet.getString(2));
                  debt.setDate_debt(resultSet.getString(3));
                  debt.setAmount_to_pay(resultSet.getString(4));
                  debt.setRemainig_value(resultSet.getString(5));
                  debts.add(debt);
              }while (resultSet.moveToNext());
          }
      }catch (SQLException e){
          Log.i("Erro: ", e.getMessage());
      }

       return debts;
    }



    // Função que realiza o pagamento de uma parcela.
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




    // Função que remove um débito
    public static boolean deleteDebt(Context context, String id){

        DebtsDbHelper helper = new DebtsDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        boolean result = false;
        ContentValues valores = new ContentValues();
        valores.put("soft_delete", 1);
        try {
            String[] args = {id};
            db.update(DebtsDbHelper.TABLE_NAME, valores, "_id = ?", args);
            result = true;
        }catch (SQLException e){
            Log.i("Erro: ", e.getMessage());
        }

        return result;
    }

}
