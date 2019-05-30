package com.jml.quemmedeve.database;

import android.provider.BaseColumns;

public final class UserContract {

    private UserContract(){}

    public static class User implements BaseColumns{

        public static final String TABLE_NAME = "users";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_LOGIN = "login";
        public static final String COLUMN_PASSWORD = "password";
    }
}
