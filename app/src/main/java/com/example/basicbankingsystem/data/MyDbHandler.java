package com.example.basicbankingsystem.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.basicbankingsystem.model.Contact;
import com.example.basicbankingsystem.params.Params;

import java.util.ArrayList;
import java.util.List;

public class MyDbHandler extends SQLiteOpenHelper {

    public MyDbHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table1 = "create table " + Params.TABLE_NAME1 + " ( " + Params.KEY_PHONE + " INTEGER PRIMARY KEY ," + Params.KEY_NAME + " TEXT," + Params.KEY_BALANCE + " DECIMAL," + Params.KEY_EMAIL + " VARCHAR," + Params.KEY_ACCOUNT_N0 + " VARCHAR," + Params.KEY_IFSC_CODE + " VARCHAR)";

        String create_table2 = "CREATE TABLE " + Params.TABLE_NAME2 + " ( " + Params.KEY_TRANSACTION_ID +
                " INTEGER PRIMARY KEY," + Params.KEY_DATE + " TEXT," + Params.KEY_FROM_NAME + " TEXT," +
                Params.KEY_TO_NAME + " TEXT," + Params.KEY_AMOUNT + " DECIMAL," + Params.KEY_STATUS + " TEXT" + ")";


        db.execSQL(create_table1);
        db.execSQL(create_table2);
    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Params.KEY_NAME, contact.getName());
        values.put(Params.KEY_PHONE, Long.parseLong(contact.getPhone_no()));
        values.put(Params.KEY_BALANCE, contact.getBalance());
        values.put(Params.KEY_EMAIL, contact.getEmail());
        values.put(Params.KEY_ACCOUNT_N0, contact.getAccount_no());
        values.put(Params.KEY_IFSC_CODE, contact.getIfsc_code());

        db.insert(Params.TABLE_NAME1, null, values);
        Log.d("dbJaimin", "Successfully inserted");
        db.close();
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
                contact.setPhone_no(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setBalance(cursor.getString(2));
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

        String select = "select * from " + Params.TABLE_NAME1 + " except select * from " + Params.TABLE_NAME1 + " where " + Params.KEY_PHONE + " = " + phonenumber;
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setPhone_no(cursor.getString(0));
                contact.setName(cursor.getString(1));
                contact.setBalance(cursor.getString(2));

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
                contact.setDate(cursor.getString(1));
                contact.setFrom_name(cursor.getString(2));
                contact.setTo_name(cursor.getString(3));
                contact.setBalance(cursor.getString(4));
                contact.setTransaction_status(cursor.getString(5));

                Log.d("dbJaimingethistory", "From: " + cursor.getString(2) + "\n"
                        + " To: " + cursor.getString(3) + "\n"
                        + " Status: " + cursor.getString(5) + "\n"
                        + "balance: " + cursor.getString(4) + "\n"
                        + "date: " + cursor.getString(1));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }


        return contactList;
    }

    public Cursor readparticulardata(String phonenumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Params.TABLE_NAME1 + " WHERE phone_no = " + phonenumber, null);
        return cursor;
    }

    public void deleteContact(String phone_no) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Params.TABLE_NAME, Params.KEY_PHONE + "=?", new String[]{phone_no});
        Log.d("dbJaimin", "delete contact successfully invoked");
        db.close();
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

    public Cursor readtransferdata() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Params.TABLE_NAME2, null);
        return cursor;
    }

    public void updateAmount(String phonenumber, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update " + Params.TABLE_NAME1 + " set balance = " + amount + " where " + Params.KEY_PHONE + " = " + phonenumber);
    }

}
