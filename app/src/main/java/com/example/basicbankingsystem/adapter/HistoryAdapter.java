package com.example.basicbankingsystem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicbankingsystem.R;
import com.example.basicbankingsystem.model.Contact;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context mContext;
    private List<Contact> mContactList;

    public HistoryAdapter(Context context, List<Contact> contactList) {
        this.mContext = context;
        this.mContactList = contactList;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        Contact contact = mContactList.get(position);

        holder.mFromNameTextView.setText(contact.getFrom_name());
        holder.mToNameTextView.setText(contact.getTo_name());
        holder.mDateTextView.setText(contact.getDate());
        holder.mAmountTextView.setText(String.valueOf(contact.getBalance()));
        holder.mStatusTextView.setText(contact.getTransaction_status());

        if (contact.getTransaction_status().equals("Failed")) {
            holder.mStatusTextView.setTextColor(Color.parseColor("#f40404"));
        } else {
            holder.mStatusTextView.setTextColor(Color.parseColor("#4BB543"));
        }
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mFromNameTextView;
        public TextView mToNameTextView;
        public TextView mDateTextView;
        public TextView mAmountTextView;
        public TextView mStatusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mFromNameTextView = itemView.findViewById(R.id.history_from_name);
            mToNameTextView = itemView.findViewById(R.id.history_to_name);
            mDateTextView = itemView.findViewById(R.id.history_date);
            mAmountTextView = itemView.findViewById(R.id.amount);
            mStatusTextView = itemView.findViewById(R.id.transaction_status);

        }

        @Override
        public void onClick(View v) {

        }
    }
}