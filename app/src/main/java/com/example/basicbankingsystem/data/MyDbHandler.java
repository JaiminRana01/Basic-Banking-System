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
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(3219876541, 'Aarav', 9472.00, 'aarav@gmail.com', 'XXXXXXXXXXXX1234', 'ABC09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9874561892,'Daksh',4554.40,'daksh@gmail.com','XXXXXXXXXXXX7890','CAB21098765')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(9873216541,'Dev',3647.055,'dev@gmail.com','XXXXXXXXXXXX1245','CBA43210987')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(6549871233,'Gautam',5522.0067,'gautam@gmail.com','XXXXXXXXXXXX7834','CDC10987654')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(7891234562,'Hardik',6416.00,'hardik@gmail.com','XXXXXXXXXXXX1278','BCA65432109')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(1597534862,'Lakshay',7389.00,'lakshay@gmail.com','XXXXXXXXXXXX4569','BCD09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(7539514865,'Neel',5296.00,'neel@gmail.com','XXXXXXXXXXXX4525','ABC09876543')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(5852456588,'Ranbir',4175.00,'ranbir@gmail.com','XXXXXXXXXXXX5859','ABC09876159')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(8978321654,'Kabir',2455.00,'kabir@gmail.com','XXXXXXXXXXXX9874','ABC09876458')");
        db.execSQL("INSERT INTO " + Params.TABLE_NAME1 + " values(7898546321,'Ashish',7643.00,'ashish@gmail.com','XXXXXXXXXXXX1478','BCA98765789')");
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
