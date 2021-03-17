package com.example.basicbankingsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicbankingsystem.adapter.SendToUserAdapter;
import com.example.basicbankingsystem.data.MyDbHandler;
import com.example.basicbankingsystem.model.Contact;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SendToUser extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private SendToUserAdapter adapter;

    MyDbHandler db = new MyDbHandler(SendToUser.this);

    String mPhoneNo, mName, mCurrentAmount;
    String mSelectuserPhoneNo, mSelectuserName, mSelectuserBalance, mDate;

    View layout;
    TextView toastTextView;
    ImageView toastImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_user);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the Layout Inflater and inflate the layout from xml
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_layout_root));

        // get the reference of TextView and ImageVIew from inflated layout
        toastTextView = layout.findViewById(R.id.toastTextView);
        toastImageView = layout.findViewById(R.id.toastImageView);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPhoneNo = bundle.getString("phone_no");
            mName = bundle.getString("name");
            mCurrentAmount = bundle.getString("current_amount");
        }


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy, hh:mm a");
        mDate = simpleDateFormat.format(calendar.getTime());

        AyncData async = new AyncData();
        async.execute();
    }

    public void selectUser(int position) {
        mSelectuserPhoneNo = contactList.get(position).getPhone_no();
        Cursor cursor = new MyDbHandler(this).readParticularData(mSelectuserPhoneNo);
        while (cursor.moveToNext()) {
            mSelectuserName = cursor.getString(1);
            mSelectuserBalance = cursor.getString(2);

            Intent intent = new Intent(SendToUser.this, TransferMoney.class);

            intent.putExtra("from_name", mName);
            intent.putExtra("to_phone_no", mSelectuserPhoneNo);
            intent.putExtra("from_phone_no", mPhoneNo);
            intent.putExtra("to_name", mSelectuserName);
            intent.putExtra("from_balance", mCurrentAmount);
            intent.putExtra("to_balance", mSelectuserBalance);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder_exitbutton = new AlertDialog.Builder(SendToUser.this);
        builder_exitbutton.setTitle("Do you want to cancel the transaction?").setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new MyDbHandler(SendToUser.this).addHistory(mDate, mName, "Not selected", "0", "Failed");
                        // set the text in the TextView
                        toastTextView.setText("Transaction Cancelled!");
                        // set the Image in the ImageView
                        toastImageView.setImageResource(R.drawable.ic_cancel);
                        // create a new Toast using context
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG); // set the duration for the Toast
                        toast.setView(layout); // set the inflated layout
                        toast.show(); // display the custom Toast
                        startActivity(new Intent(SendToUser.this, UsersActivity.class));
                        finish();
                    }
                }).setNegativeButton("No", null);
        AlertDialog alertexit = builder_exitbutton.create();
        alertexit.show();
    }

    private class AyncData extends AsyncTask<Void, Void, List<Contact>> {

        @Override
        protected List<Contact> doInBackground(Void... voids) {

            contactList = db.getSendToUserData(mPhoneNo);
            return contactList;
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            super.onPostExecute(contacts);
            adapter = new SendToUserAdapter(SendToUser.this, contacts);
            recyclerView.setAdapter(adapter);
        }
    }
}