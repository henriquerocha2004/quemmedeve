package com.jml.quemmedeve.ultility;

import android.content.Intent;
import android.provider.CalendarContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUltility {
    private static SimpleDateFormat formatoBR = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat formatoUSA = new SimpleDateFormat( "yyyy-MM-dd");
    private static SimpleDateFormat formatoParaCalendário = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");


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

    //Função que insere o evento no calendário.

    public static Intent setEventOnCalendar(List<String> datas, String[] dados) throws ParseException {

            int quantidadeDeDatas = datas.size();

            String dataPrimeiraParcela = datas.get(0);
            String dataUltimaParcela = datas.get(quantidadeDeDatas - 1);

            Date dia = formatoUSA.parse(dataPrimeiraParcela);
            int diaPrimeiraParcela = dia.getDay();
            System.out.println(diaPrimeiraParcela);

            Date dataFinalUsa = formatoUSA.parse(dataUltimaParcela);

            Calendar cal = Calendar.getInstance();
            cal.setTime(dataFinalUsa);
            cal.add(Calendar.DAY_OF_MONTH, 3);
            String dataFinal = formatoParaCalendário.format(cal.getTime());
            System.out.println(String.format("FREQ=MONTHLY;INTERVAL=1;BYMONTHDAY=%s;UNTIL=%s", diaPrimeiraParcela, dataFinal));

            cal.setTime(dia);
            long time = cal.getTimeInMillis();

            Intent calIntent = new Intent(Intent.ACTION_INSERT);
            calIntent.setData(CalendarContract.Events.CONTENT_URI);
            calIntent.setType("vnd.android.cursor.item/event");
            calIntent.putExtra(CalendarContract.Events.TITLE, "Dívida de "+dados[0]);
            calIntent.putExtra(CalendarContract.Events.DESCRIPTION, dados[1] + " - Valor parcelado a cobrar: R$ "+ dados[2]+", dividido em "+ dados[3]+" vezes.");
            calIntent.putExtra(CalendarContract.Events.EVENT_TIMEZONE, java.util.Locale.getDefault());
            calIntent.putExtra("beginTime", time + 36000000);
            calIntent.putExtra("endTime", time + 36000000 + 1800000);
            calIntent.putExtra(CalendarContract.Events.RRULE, String.format("FREQ=MONTHLY;INTERVAL=1;BYMONTHDAY=%s;UNTIL=%s", diaPrimeiraParcela, dataFinal));

            return calIntent;
    }
}
