package com.example.basicbankingsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.basicbankingsystem.adapter.HistoryAdapter;
import com.example.basicbankingsystem.data.MyDbHandler;
import com.example.basicbankingsystem.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class HistoryListActivity extends AppCompatActivity {

    List<Contact> historyList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HistoryAdapter adapter;

    TextView historyEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        MyDbHandler db = new MyDbHandler(HistoryListActivity.this);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        historyEmpty = findViewById(R.id.empty_text);

        historyList = db.getHistoryData();

        for (Contact contact : historyList) {
            Log.d("dbJaimin", "From: " + contact.getFrom_name() + "\n"
                    + " To: " + contact.getTo_name() + "\n"
                    + " Status: " + contact.getTransaction_status() + "\n"
                    + "balance: " + contact.getBalance() + "\n"
                    + "date: " + contact.getDate());
        }

        adapter = new HistoryAdapter(HistoryListActivity.this, historyList);
        recyclerView.setAdapter(adapter);

        if (historyList.size() == 0) {
            historyEmpty.setVisibility(View.VISIBLE);
        }

    }
}