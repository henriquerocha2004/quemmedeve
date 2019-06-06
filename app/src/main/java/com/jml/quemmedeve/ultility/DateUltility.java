package com.jml.quemmedeve.ultility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUltility {

    private static SimpleDateFormat formatoBR = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat formatoUSA = new SimpleDateFormat( "yyyy-MM-dd");


    public static String formataBR(String date){

        try {
            Date dataUSA = formatoUSA.parse(date);
            String dataBR = formatoBR.format(dataUSA);
            return dataBR;

        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String formataUSA(String date){
        try {
            Date dataBR = formatoBR.parse(date);
            String dataUSA = formatoUSA.format(dataBR);
            return dataUSA;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String getCurrentData(String formato){
        Date data = new Date();
        return formato == "USA" ? formatoUSA.format(data) : formatoBR.format(data);
    }

    public static String gerarProximaDataDePagamento(String data)throws ParseException{
        Calendar cal = Calendar.getInstance();
        cal.setTime(formatoUSA.parse(data));
        cal.add(cal.MONTH, 1);
        Date d = cal.getTime();
        String dt = formatoUSA.format(d);
        return dt;
    }
}
