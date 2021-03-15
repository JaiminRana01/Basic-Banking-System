package com.example.basicbankingsystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicbankingsystem.adapter.UserListAdapter;
import com.example.basicbankingsystem.data.MyDbHandler;
import com.example.basicbankingsystem.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private UserListAdapter adapter;

    MyDbHandler db = new MyDbHandler(UsersActivity.this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AyncData async = new AyncData();
        async.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_history) {
            startActivity(new Intent(UsersActivity.this, HistoryListActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private class AyncData extends AsyncTask<Void, Void, List<Contact>> {

        @Override
        protected List<Contact> doInBackground(Void... voids) {

            contactList = db.getAllData();
            return contactList;
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            super.onPostExecute(contacts);
            adapter = new UserListAdapter(UsersActivity.this, contacts);
            recyclerView.setAdapter(adapter);
        }
    }
}
