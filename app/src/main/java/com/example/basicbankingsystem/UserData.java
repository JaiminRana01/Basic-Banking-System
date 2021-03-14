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

    TextView mNameTextView, mPhoneNoTextView, mEmailIdTextView, mAccountNoTextView, mIfscCodeTextView, mCurrentBalanceTextView;
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
                enterAmountDialog();
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

    private void enterAmountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserData.this);

        View dialogView = getLayoutInflater().inflate(R.layout.transfer, null);
        builder.setTitle("Enter amount");
        builder.setCancelable(false);
        builder.setView(dialogView);

        EditText mAmount = (EditText) dialogView.findViewById(R.id.enter_amount);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        }).

                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        transactionCancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAmount.getText().toString().isEmpty()) {
                    mAmount.setError("Amount can't be empty");
                } else if (Double.parseDouble(mAmount.getText().toString()) > mCurrentBalance) {
                    mAmount.setError("Your account don't have enough balance");
                } else {
                    Intent intent = new Intent(UserData.this, SendToUser.class);
                    intent.putExtra("phone_no", mPhoneNoTextView.getText().toString());
                    intent.putExtra("name", mNameTextView.getText().toString());
                    intent.putExtra("current_amount", String.valueOf(mCurrentBalance));
                    intent.putExtra("transfer_amount", mAmount.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void transactionCancel() {
        AlertDialog.Builder builder_exitbutton = new AlertDialog.Builder(UserData.this);
        builder_exitbutton.setTitle("Do you want to cancel the transaction?").setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy, hh:mm a");
                        String date = simpleDateFormat.format(calendar.getTime());

                        new MyDbHandler(UserData.this).addHistory(date, mNameTextView.getText().toString(), "Not selected", "0", "Failed");

                        Toast.makeText(UserData.this, "Transaction Cancelled!", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterAmountDialog();
            }
        });
        AlertDialog alertexit = builder_exitbutton.create();
        alertexit.show();
    }
}