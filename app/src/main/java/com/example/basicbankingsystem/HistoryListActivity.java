package com.example.basicbankingsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.basicbankingsystem.adapter.HistoryAdapter;
import com.example.basicbankingsystem.data.MyDbHandler;
import com.example.basicbankingsystem.model.Contact;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryListActivity extends AppCompatActivity {

    List<Contact> historyList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    HistoryAdapter adapter;

    TextView history_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        MyDbHandler db = new MyDbHandler(HistoryListActivity.this);

//        Contact contact1 = new Contact(0, "11-Mar-2021, 03:58 PM", "Failed", " Deepanshu", " Not selected");
//        db.addHistory(contact1);


        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        history_empty = findViewById(R.id.empty_text);

        historyList = db.getHistoryData();

        for (Contact contact : historyList) {
            Log.d("dbJaimin", "From: " + contact.getFrom_name() + "\n"
                    + " To: " + contact.getTo_name() + "\n"
                    + " Status: " + contact.getTransaction_status() + "\n"
                    + "balance: " + contact.getBalance() + "\n"
                    + "date: " + contact.getDate());
        }

        adapter = new HistoryAdapter(HistoryListActivity.this, historyList);
        mRecyclerView.setAdapter(adapter);

        if (historyList.size() == 0) {
            history_empty.setVisibility(View.VISIBLE);
        }

    }

    private void showData() {
        historyList.clear();
        Cursor cursor = new MyDbHandler(this).readtransferdata();

        while (cursor.moveToNext()) {
            String balancefromdb = cursor.getString(4);
            Double balance = Double.parseDouble(balancefromdb);

            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setGroupingUsed(true);
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            String price = nf.format(balance);

            Contact contact = new Contact(cursor.getString(4), cursor.getString(1), cursor.getString(5), cursor.getString(2), cursor.getString(3));
            historyList.add(contact);
        }

        adapter = new HistoryAdapter(HistoryListActivity.this, historyList);
        mRecyclerView.setAdapter(adapter);

        if (historyList.size() == 0) {
            history_empty.setVisibility(View.VISIBLE);
        }

    }
}