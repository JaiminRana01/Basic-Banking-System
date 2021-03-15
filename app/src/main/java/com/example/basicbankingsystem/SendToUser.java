package com.example.basicbankingsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.basicbankingsystem.adapter.SendToUserAdapter;
import com.example.basicbankingsystem.adapter.UserListAdapter;
import com.example.basicbankingsystem.data.MyDbHandler;
import com.example.basicbankingsystem.model.Contact;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SendToUser extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private SendToUserAdapter adapter;

    MyDbHandler db = new MyDbHandler(SendToUser.this);

    String mPhoneNo, mName, mCurrentAmount, mTransferAmount, mRemainingAmount;
    String mSelectuserPhoneNo, mSelectuserName, mSelectuserBalance, mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_user);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPhoneNo = bundle.getString("phone_no");
            mName = bundle.getString("name");
            mCurrentAmount = bundle.getString("current_amount");
            mTransferAmount = bundle.getString("transfer_amount");
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
            Double selecteduserBalance = Double.parseDouble(mSelectuserBalance);
            Double selecteduserTransferAmount = Double.parseDouble(mTransferAmount);
            Double selecteduserRemainingAmount = selecteduserBalance + selecteduserTransferAmount;

            new MyDbHandler(this).addHistory(mDate, mName, mSelectuserName, mTransferAmount, "Success");
            new MyDbHandler(this).updateAmount(mSelectuserPhoneNo, selecteduserRemainingAmount.toString());
            calculateAmount();
            Toast.makeText(this, "Transaction Successful!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SendToUser.this, UsersActivity.class));
            finish();
        }
    }

    private void calculateAmount() {
        Double currentAmount = Double.parseDouble(mCurrentAmount);
        Double transferAmount = Double.parseDouble(mTransferAmount);
        Double remainingAmount = currentAmount - transferAmount;
        mRemainingAmount = remainingAmount.toString();
        new MyDbHandler(this).updateAmount(mPhoneNo, mRemainingAmount);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder_exitbutton = new AlertDialog.Builder(SendToUser.this);
        builder_exitbutton.setTitle("Do you want to cancel the transaction?").setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new MyDbHandler(SendToUser.this).addHistory(mDate, mName, "Not selected", mTransferAmount, "Failed");
                        Toast.makeText(SendToUser.this, "Transaction Cancelled!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SendToUser.this, UsersActivity.class));
                        finish();
                    }
                }).setNegativeButton("No", null);
        AlertDialog alertexit = builder_exitbutton.create();
        alertexit.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Contact> newList = new ArrayList<>();
                for (Contact model : contactList) {
                    String name = model.getName().toLowerCase();
                    if (name.contains(newText)) {
                        newList.add(model);
                    }
                }
                adapter.setFilter(newList);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
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