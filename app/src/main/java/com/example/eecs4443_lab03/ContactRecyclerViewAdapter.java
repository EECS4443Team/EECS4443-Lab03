package com.example.eecs4443_lab03;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eecs4443_lab03.databinding.FragmentContactBinding;
import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder> {

    private List<Contact> mValues;
    private final OnContactClickListener mListener;

    public interface OnContactClickListener {
        void onContactTap(Contact item);
        void onContactLongPress(Contact item);
    }

    public ContactRecyclerViewAdapter(List<Contact> items, OnContactClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void updateItems(List<Contact> newItems) {
        this.mValues = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentContactBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(holder.mItem.getName());
        holder.mContentView.setText(holder.mItem.getPhoneNumber());

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onContactTap(holder.mItem);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mListener != null) {
                mListener.onContactLongPress(holder.mItem);
                return true; // Consume the long click
            }
            return false;
        });
    }

    @Override
    public int getItemCount() { return mValues.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public Contact mItem;

        public ViewHolder(FragmentContactBinding binding) {
            super(binding.getRoot());
            mIdView = binding.listContactName;
            mContentView = binding.listContactPhone;
        }
    }
}