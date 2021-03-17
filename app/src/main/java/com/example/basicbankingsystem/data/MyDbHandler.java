package com.example.basicbankingsystem.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.basicbankingsystem.model.Contact;
import com.example.basicbankingsystem.params.Params;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyDbHandler extends SQLiteOpenHelper {

    public MyDbHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table1 = "CREATE TABLE " + Params.TABLE_NAME1 + " ( " + Params.KEY_PHONE + " INTEGER PRIMARY KEY ," + Params.KEY_NAME + " TEXT,"
                + Params.KEY_BALANCE + " DOUBLE," + Params.KEY_EMAIL + " VARCHAR," + Params.KEY_ACCOUNT_N0 + " VARCHAR," + Params.KEY_IFSC_CODE + " VARCHAR)";

        String create_table2 = "CREATE TABLE " + Params.TABLE_NAME2 + " ( " + Params.KEY_TRANSACTION_ID +
                " INTEGER PRIMARY KEY," + Params.KEY_DATE + " TEXT," + Params.KEY_FROM_NAME + " TEXT," +
                Params.KEY_TO_NAME + " TEXT," + Params.KEY_AMOUNT + " DOUBLE, " + Params.KEY_STATUS + " TEXT" + ")";


        db.execSQL(create_table1);
        db.execSQL(create_table2);
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9000000001, 'Bhavik', 9472.00, 'harshit@gmail.com', 'XXXXXXXXXXXX1234', 'ABC09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9000000002,'Aaryan',9472.40,'harshit@gmail.com','XXXXXXXXXXXX1234','ABC09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9000000003,'Rutvik',9472.055,'harshit@gmail.com','XXXXXXXXXXXX1234','ABC09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9000000004,'Pranjal',9472.0067,'harshit@gmail.com','XXXXXXXXXXXX1234','ABC09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9000000005,'Rushabh',9472.00,'harshit@gmail.com','XXXXXXXXXXXX1234','ABC09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9000000006,'JK',9472.00,'harshit@gmail.com','XXXXXXXXXXXX1234','ABC09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9000000007,'Deep',9472.00,'harshit@gmail.com','XXXXXXXXXXXX1234','ABC09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9000000008,'Rounak',9472.00,'harshit@gmail.com','XXXXXXXXXXXX1234','ABC09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9000000009,'Aman',9472.00,'harshit@gmail.com','XXXXXXXXXXXX1234','ABC09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9000000020,'Naman',9472.00,'harshit@gmail.com','XXXXXXXXXXXX1234','ABC09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9000000030,'Shubham',9472.00,'harshit@gmail.com','XXXXXXXXXXXX1234','ABC09876543')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Params.TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + Params.TABLE_NAME2);
        onCreate(db);
    }

    public List<Contact> getAllData() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + Params.TABLE_NAME1;
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();

                String balanceFromDb = cursor.getString(2);
                Double balance = Double.parseDouble(balanceFromDb);

                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setGroupingUsed(true);
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);
                String price = nf.format(balance);

                contact.setPhone_no(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setBalance(price);
                contact.setEmail(cursor.getString(3));
                contact.setAccount_no(cursor.getString(4));
                contact.setIfsc_code(cursor.getString(5));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public List<Contact> getSendToUserData(String phonenumber) {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + Params.TABLE_NAME1 + " EXCEPT SELECT * FROM " + Params.TABLE_NAME1 + " WHERE " + Params.KEY_PHONE + " = " + phonenumber;
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                String balanceFromDb = cursor.getString(2);
                Double balance = Double.parseDouble(balanceFromDb);

                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setGroupingUsed(true);
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);
                String price = nf.format(balance);

                contact.setPhone_no(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setBalance(price);

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public List<Contact> getHistoryData() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + Params.TABLE_NAME2;
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();

                String balanceFromDb = cursor.getString(4);
                Double balance = Double.parseDouble(balanceFromDb);

                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setGroupingUsed(true);
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);
                String price = nf.format(balance);

                contact.setDate(cursor.getString(1));
                contact.setFrom_name(cursor.getString(2));
                contact.setTo_name(cursor.getString(3));
                contact.setBalance(price);
                contact.setTransaction_status(cursor.getString(5));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        Collections.reverse(contactList);
        return contactList;
    }

    public Cursor readParticularData(String phone_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Params.TABLE_NAME1 + " WHERE " + Params.KEY_PHONE + " = " + phone_no, null);
        return cursor;
    }

    public boolean addHistory(String date, String from_name, String to_name, String amount, String status) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(Params.KEY_DATE, date);
        contentValues.put(Params.KEY_FROM_NAME, from_name);
        contentValues.put(Params.KEY_TO_NAME, to_name);
        contentValues.put(Params.KEY_AMOUNT, amount);
        contentValues.put(Params.KEY_STATUS, status);
        Long result = db.insert(Params.TABLE_NAME2, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void updateAmount(String phone_no, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + Params.TABLE_NAME1 + " SET balance = " + amount + " WHERE " + Params.KEY_PHONE + " = " + phone_no);
    }


}
