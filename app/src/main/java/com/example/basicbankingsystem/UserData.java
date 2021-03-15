package com.example.basicbankingsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicbankingsystem.data.MyDbHandler;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserData extends AppCompatActivity {

    TextView mNameTextView, mPhoneNoTextView, mEmailIdTextView, mAccountNoTextView, mIfscCodeTextView, mCurrentBalanceTextView, mDate;
    Button mTransferButton;
    String mPhoneNo;
    double mCurrentBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        mNameTextView = findViewById(R.id.username);
        mPhoneNoTextView = findViewById(R.id.user_phone_no);
        mEmailIdTextView = findViewById(R.id.email);
        mAccountNoTextView = findViewById(R.id.account_no);
        mIfscCodeTextView = findViewById(R.id.ifsc_code);
        mCurrentBalanceTextView = findViewById(R.id.balance);
        mTransferButton = findViewById(R.id.button_transfer);

        Intent intent = getIntent();
        mPhoneNo = intent.getStringExtra("Phone_no");
        showData(mPhoneNo);

        mTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserData.this, SendToUser.class);
                intent.putExtra("phone_no", mPhoneNoTextView.getText().toString());
                intent.putExtra("name", mNameTextView.getText().toString());
                intent.putExtra("current_amount", String.valueOf(mCurrentBalance));
                startActivity(intent);
                finish();
            }
        });

    }

    private void showData(String phoneNo) {
        Cursor cursor = new MyDbHandler(this).readParticularData(phoneNo);

        if (cursor.moveToFirst()) {
            do {
                String balanceFromDb = cursor.getString(2);
                mCurrentBalance = Double.parseDouble(balanceFromDb);

                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setGroupingUsed(true);
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);
                String price = nf.format(mCurrentBalance);

                mPhoneNoTextView.setText(cursor.getString(0));
                mNameTextView.setText(cursor.getString(1));
                mCurrentBalanceTextView.setText(price);
                mEmailIdTextView.setText(cursor.getString(3));
                mAccountNoTextView.setText(cursor.getString(4));
                mIfscCodeTextView.setText(cursor.getString(5));
            } while (cursor.moveToNext());
        }
    }

}