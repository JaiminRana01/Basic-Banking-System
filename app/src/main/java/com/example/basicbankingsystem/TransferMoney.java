package com.example.basicbankingsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicbankingsystem.data.MyDbHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TransferMoney extends AppCompatActivity {
    private Button mCancelButton;
    private Button mSendButton;

    String mPhoneNo, mName, mCurrentAmount, mTransferAmount, mRemainingAmount;
    String mSelectuserPhoneNo, mSelectuserName, mSelectuserBalance, mDate;

    TextView mSendTo;
    TextView mSendPhoneno;
    EditText mAmount;

    View layout;
    TextView toastTextView;
    ImageView toastImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);

        // Retrieve the Layout Inflater and inflate the layout from xml
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        // get the reference of TextView and ImageVIew from inflated layout
        toastTextView = (TextView) layout.findViewById(R.id.toastTextView);
        toastImageView = (ImageView) layout.findViewById(R.id.toastImageView);

        mAmount = findViewById(R.id.transfer_amount);

        mCancelButton = findViewById(R.id.cancel);
        mSendButton = findViewById(R.id.send);
        mSendTo = findViewById(R.id.send_to);
        mSendPhoneno = findViewById(R.id.phone_number);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mName = bundle.getString("from_name");
            mSelectuserPhoneNo = bundle.getString("to_phone_no");
            mPhoneNo = bundle.getString("from_phone_no");
            mSelectuserName = bundle.getString("to_name");
            mCurrentAmount = bundle.getString("from_balance");
            mSelectuserBalance = bundle.getString("to_balance");
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy, hh:mm a");
        mDate = simpleDateFormat.format(calendar.getTime());

        mSendTo.setText(mSelectuserName);
        mSendPhoneno.setText(mPhoneNo);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder_exitbutton = new AlertDialog.Builder(TransferMoney.this);
                builder_exitbutton.setTitle("Do you want to cancel the transaction?").setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new MyDbHandler(TransferMoney.this).addHistory(mDate, mName, "Not selected", "0", "Failed");
                                // set the text in the TextView
                                toastTextView.setText("Transaction Cancelled!");
                                // set the Image in the ImageView
                                toastImageView.setImageResource(R.drawable.ic_cancel);
                                // create a new Toast using context
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_LONG); // set the duration for the Toast
                                toast.setView(layout); // set the inflated layout
                                toast.show(); // display the custom Toast

                                startActivity(new Intent(TransferMoney.this, UsersActivity.class));
                                finish();
                            }
                        }).setNegativeButton("No", null);
                AlertDialog alertexit = builder_exitbutton.create();
                alertexit.show();
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAmount.getText().toString().isEmpty()) {
                    mAmount.setError("Amount can't be empty");
                } else if (Double.parseDouble(mAmount.getText().toString()) > Double.parseDouble(mCurrentAmount)) {
                    mAmount.setError("Your account don't have enough balance");
                } else {
                    selectUser();
                    finish();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder_exitbutton = new AlertDialog.Builder(TransferMoney.this);
        builder_exitbutton.setTitle("Do you want to cancel the transaction?").setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new MyDbHandler(TransferMoney.this).addHistory(mDate, mName, "Not selected", "0", "Failed");
//                        Toast.makeText(TransferMoney.this, "Transaction Cancelled!", Toast.LENGTH_LONG).show();
                        // set the text in the TextView
                        toastTextView.setText("Transaction Cancelled!");
                        // set the Image in the ImageView
                        toastImageView.setImageResource(R.drawable.ic_cancel);
                        // create a new Toast using context
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG); // set the duration for the Toast
                        toast.setView(layout); // set the inflated layout
                        toast.show(); // display the custom Toast
                        startActivity(new Intent(TransferMoney.this, UsersActivity.class));
                        finish();
                    }
                }).setNegativeButton("No", null);
        AlertDialog alertexit = builder_exitbutton.create();
        alertexit.show();
    }

    public void selectUser() {
        mTransferAmount = mAmount.getText().toString();
        Double selecteduserBalance = Double.parseDouble(mSelectuserBalance);
        Double selecteduserTransferAmount = Double.parseDouble(mTransferAmount);
        Double selecteduserRemainingAmount = selecteduserBalance + selecteduserTransferAmount;

        new MyDbHandler(this).addHistory(mDate, mName, mSelectuserName, mTransferAmount, "Success");
        new MyDbHandler(this).updateAmount(mSelectuserPhoneNo, selecteduserRemainingAmount.toString());
        calculateAmount();
//        Toast.makeText(this, "Transaction Successful!", Toast.LENGTH_LONG).show();


        // set the text in the TextView
        toastTextView.setText("Transaction Successful");
        // set the Image in the ImageView
        toastImageView.setImageResource(R.drawable.ic_check);
        // create a new Toast using context
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG); // set the duration for the Toast
        toast.setView(layout); // set the inflated layout
        toast.show(); // display the custom Toast
        startActivity(new Intent(TransferMoney.this, UsersActivity.class));
        finish();
    }

    private void calculateAmount() {
        Double currentAmount = Double.parseDouble(mCurrentAmount);
        Double transferAmount = Double.parseDouble(mTransferAmount);
        Double remainingAmount = currentAmount - transferAmount;
        mRemainingAmount = remainingAmount.toString();
        new MyDbHandler(this).updateAmount(mPhoneNo, mRemainingAmount);
    }

}