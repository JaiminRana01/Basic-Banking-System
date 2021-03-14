package com.example.basicbankingsystem.params;

public class Params {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "bank.db";
    public static final String TABLE_NAME1 = "user_table";
    public static final String TABLE_NAME2 = "transfers_table";

    //keys of user_table
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone_no";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_BALANCE = "balance";
    public static final String KEY_ACCOUNT_N0 = "account_no";
    public static final String KEY_IFSC_CODE = "ifsc_code";

    //keys of transfer_table
    public static final String KEY_TRANSACTION_ID = "transaction_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_FROM_NAME = "from_name";
    public static final String KEY_TO_NAME = "to_name";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_STATUS = "status";
}
