package com.example.basicbankingsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
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
    MyDbHandler db = new MyDbHandler(HistoryListActivity.this);
    TextView historyEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        historyEmpty = findViewById(R.id.empty_text);

        AyncData async = new AyncData();
        async.execute();
    }

    private class AyncData extends AsyncTask<Void, Void, List<Contact>> {

        @Override
        protected List<Contact> doInBackground(Void... voids) {

            historyList = db.getHistoryData();
            return historyList;
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            super.onPostExecute(contacts);
            if (historyList.size() == 0) {
                historyEmpty.setVisibility(View.VISIBLE);
            }

            adapter = new HistoryAdapter(HistoryListActivity.this, contacts);
            recyclerView.setAdapter(adapter);
        }
    }
}