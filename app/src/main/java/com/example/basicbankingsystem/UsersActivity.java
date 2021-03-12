package com.example.basicbankingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicbankingsystem.adapter.RecyclerViewAdapter;
import com.example.basicbankingsystem.data.MyDbHandler;
import com.example.basicbankingsystem.model.Contact;
import com.example.basicbankingsystem.params.Params;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Contact> contactArrayList;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyDbHandler db = new MyDbHandler(UsersActivity.this);

        contactArrayList = new ArrayList<>();

        Contact contact1 = new Contact("9000000000", "Bhavik", "9472.00", "harshit@gmail.com", "XXXXXXXXXXXX1234", "ABC09876543");
        db.addContact(contact1);

        Contact contact2 = new Contact("9111111112", "Aaryan", "5582.6", "ashok@gmail.com", "XXXXXXXXXXXX2341", "BCA98765432");
        db.addContact(contact2);

        Contact contact3 = new Contact("9111111113", "Aman", "7582.6", "ashok@gmail.com", "XXXXXXXXXXXX2341", "BCA98765432");
        db.addContact(contact3);

        Contact contact4 = new Contact("9111111114", "Ronak", "6582.6", "ashok@gmail.com", "XXXXXXXXXXXX2341", "BCA98765432");
        db.addContact(contact4);

        Contact contact5 = new Contact("9111111115", "Rutvikraj", "10582.6", "ashok@gmail.com", "XXXXXXXXXXXX2341", "BCA98765432");
        db.addContact(contact5);

        Contact contact6 = new Contact("9111111116", "Rushabh", "12582.6", "ashok@gmail.com", "XXXXXXXXXXXX2341", "BCA98765432");
        db.addContact(contact6);

        Contact contact7 = new Contact("9111111117", "Deep", "7582.6", "ashok@gmail.com", "XXXXXXXXXXXX2341", "BCA98765432");
        db.addContact(contact7);

        Contact contact8 = new Contact("9111111118", "JK", "8582.6", "ashok@gmail.com", "XXXXXXXXXXXX2341", "BCA98765432");
        db.addContact(contact8);

        Contact contact9 = new Contact("9111111119", "Ashok", "10582.6", "ashok@gmail.com", "XXXXXXXXXXXX2341", "BCA98765432");
        db.addContact(contact9);

        Contact contact10 = new Contact("9111111128", "Jaydev", "11582.6", "ashok@gmail.com", "XXXXXXXXXXXX2341", "BCA98765432");
        db.addContact(contact10);

        contactArrayList = db.getAllData();

        recyclerViewAdapter = new RecyclerViewAdapter(UsersActivity.this, contactArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
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
}
