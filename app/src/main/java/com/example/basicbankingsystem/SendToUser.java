package com.example.basicbankingsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.basicbankingsystem.adapter.SendToUserAdapter;
import com.example.basicbankingsystem.data.MyDbHandler;
import com.example.basicbankingsystem.model.Contact;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SendToUser extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SendToUserAdapter adapter;
    private List<Contact> contactList = new ArrayList<>();

    String phonenumber, name, currentamount, transferamount, remainingamount;
    String mSelectuserPhonenumber, mSelectuserName, mSelectuserBalance, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_user);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyDbHandler db = new MyDbHandler(SendToUser.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phonenumber = bundle.getString("phone_no");
            name = bundle.getString("name");
            currentamount = bundle.getString("current_amount");
            transferamount = bundle.getString("transfer_amount");
        }

        contactList = db.getSendToUserData(phonenumber);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy, hh:mm a");
        date = simpleDateFormat.format(calendar.getTime());


        adapter = new SendToUserAdapter(SendToUser.this, contactList);
        recyclerView.setAdapter(adapter);
    }

    public void selectuser(int position) {
        mSelectuserPhonenumber = contactList.get(position).getPhone_no();
        Cursor cursor = new MyDbHandler(this).readparticulardata(mSelectuserPhonenumber);
        while (cursor.moveToNext()) {
            mSelectuserName = cursor.getString(1);
            mSelectuserBalance = cursor.getString(2);
            Double Dselectuser_balance = Double.parseDouble(mSelectuserBalance);
            Double Dselectuser_transferamount = Double.parseDouble(transferamount);
            Double Dselectuser_remainingamount = Dselectuser_balance + Dselectuser_transferamount;

            new MyDbHandler(this).addHistory(date, name, mSelectuserName, transferamount, "Success");
            new MyDbHandler(this).updateAmount(mSelectuserPhonenumber, Dselectuser_remainingamount.toString());
            calculateAmount();
            Toast.makeText(this, "Transaction Successful!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SendToUser.this, UsersActivity.class));
            finish();
        }
    }

    private void calculateAmount() {
        Double Dcurrentamount = Double.parseDouble(currentamount);
        Double Dtransferamount = Double.parseDouble(transferamount);
        Double Dremainingamount = Dcurrentamount - Dtransferamount;
        remainingamount = Dremainingamount.toString();
        new MyDbHandler(this).updateAmount(phonenumber, remainingamount);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder_exitbutton = new AlertDialog.Builder(SendToUser.this);
        builder_exitbutton.setTitle("Do you want to cancel the transaction?").setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new MyDbHandler(SendToUser.this).addHistory(date, name, "Not selected", transferamount, "Failed");
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
}