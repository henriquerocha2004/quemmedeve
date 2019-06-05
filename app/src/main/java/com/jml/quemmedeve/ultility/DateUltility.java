package com.jml.quemmedeve.ultility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUltility {

    private static SimpleDateFormat formatoBR = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat formatoUSA = new SimpleDateFormat( "yyyy-MM-dd");


    public static String formataBR(String date){
        String data = formatoBR.format(Date.parse(date));
        return data;
    }

    public static String formataUSA(String date){
        String data = formatoUSA.format(Date.parse(date));
        return data;
    }

    public static String getCurrentData(String formato){
        Date data = new Date(System.currentTimeMillis());
        String stringData = data.toString();
        return formato == "USA" ? formataUSA(stringData) : formataBR(stringData);
    }
}
