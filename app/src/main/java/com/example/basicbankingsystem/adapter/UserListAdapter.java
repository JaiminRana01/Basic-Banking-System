package com.example.basicbankingsystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicbankingsystem.R;
import com.example.basicbankingsystem.UserData;
import com.example.basicbankingsystem.model.Contact;

import java.util.List;

/**
 * An {@link UserListAdapter} knows how to create a list item layout for each contact
 * in the data source (a list of {@link Contact} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like card view
 * to be displayed to the user.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private Context mContext;
    private List<Contact> mContactList;

    public UserListAdapter(Context context, List<Contact> contactList) {
        this.mContext = context;
        this.mContactList = contactList;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        Contact contact = mContactList.get(position);

        holder.mNameTextView.setText(contact.getName());
        holder.mPhoneNoTextView.setText(contact.getPhone_no());
        holder.mBalanceTextView.setText(String.valueOf(contact.getBalance()));
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
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
            Contact contact = mContactList.get(position);
            String phone_no = contact.getPhone_no();

            Intent intent = new Intent(mContext, UserData.class);
            intent.putExtra("Phone_no", phone_no);
            mContext.startActivity(intent);
        }
    }
}