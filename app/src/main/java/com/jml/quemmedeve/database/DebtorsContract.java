package com.jml.quemmedeve.database;

import android.provider.BaseColumns;

public final class DebtorsContract {

    private DebtorsContract(){}

    public static class Debtors implements BaseColumns{

        public static final String TABLE_NAME = "debtors";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_ID = "id";

    }

}
