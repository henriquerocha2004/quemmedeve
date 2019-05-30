package com.jml.quemmedeve.database;

import android.provider.BaseColumns;

final public class PaymentContract {

    private PaymentContract(){}

    public static class Payment implements BaseColumns {
        public static String TABLE_NAME = "payment";
        public static String COLUMN_ID = "id";
        public static String COLUMN_DEBT_ID = "debt_id";
        public static String COLUMN_AMOUNT_PAY = "amount_to_pay";
        public static String COLUMN_PAYDAY = "payday";
        public static String COLUMN_STATUS_PAYMENT = "status_payment";
    }

}
