package com.jml.quemmedeve.database;

import android.provider.BaseColumns;

public final class DebtsContract {

    private DebtsContract(){};

    public static class Debts implements BaseColumns{

        public static final String TABLE_NAME = "debts";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_USU_ID = "usu_id_debt";
        public static final String COLUMN_DEBT_DESC = "debt_desc";
        public static final String COLUMN_VALUE = "value";
        public static final String COLUMN_DATE_DEBT = "date_debt";
        public static final String COLUMN_DEBT_SPLIT = "debt_split";
        public static final String COLUMN_VALUE_SPLIT = "value_split";
    }


}
