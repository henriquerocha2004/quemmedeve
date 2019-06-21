package com.jml.quemmedeve.ultility;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtility {

    private static Locale ptBR = new Locale("pt", "BR");

    public static String converterBr(String value){
        return NumberFormat.getCurrencyInstance().format(Double.parseDouble(String.valueOf(value)));
    }

}
