package com.example.basicbankingsystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicbankingsystem.R;
import com.example.basicbankingsystem.UserData;
import com.example.basicbankingsystem.UsersActivity;
import com.example.basicbankingsystem.model.Contact;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Contact> contactList;

    public RecyclerViewAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        holder.name.setText(contact.getName());
        holder.phoneNumber.setText(contact.getPhone_no());
        holder.balance.setText(String.valueOf(contact.getBalance()));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView phoneNumber;
        public TextView balance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.username);
            phoneNumber = itemView.findViewById(R.id.phone_number);
            balance = itemView.findViewById(R.id.balance);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Contact contact = contactList.get(position);
            String phone_no = contact.getPhone_no();

            Intent intent = new Intent(context, UserData.class);
            Log.d("msgJaimin", "this is RecyclerviewAdapter");
            intent.putExtra("Phone_no", phone_no);
            context.startActivity(intent);
        }
    }
}