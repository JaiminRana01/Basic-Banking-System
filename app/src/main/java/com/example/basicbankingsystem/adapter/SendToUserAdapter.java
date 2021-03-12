package com.example.basicbankingsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicbankingsystem.R;
import com.example.basicbankingsystem.SendToUser;
import com.example.basicbankingsystem.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class SendToUserAdapter extends RecyclerView.Adapter<SendToUserAdapter.ViewHolder> {
    private Context context;
    private List<Contact> contactList;
    private SendToUser sendToUser;

    public SendToUserAdapter(SendToUser sendToUser, List<Contact> contactList) {
        this.sendToUser = sendToUser;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public SendToUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SendToUserAdapter.ViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        holder.mNameTextView.setText(contact.getName());
        holder.mPhoneNoTextView.setText(contact.getPhone_no());
        holder.mBalanceTextView.setText(contact.getBalance());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mNameTextView;
        public TextView mPhoneNoTextView;
        public TextView mBalanceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mNameTextView = itemView.findViewById(R.id.username);
            mPhoneNoTextView = itemView.findViewById(R.id.phone_number);
            mBalanceTextView = itemView.findViewById(R.id.balance);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            sendToUser.selectuser(position);
        }
    }

    public void setFilter(ArrayList<Contact> newList) {
        contactList = new ArrayList<>();
        contactList.addAll(newList);
        notifyDataSetChanged();
    }
}