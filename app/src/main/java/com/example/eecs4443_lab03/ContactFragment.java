package com.example.eecs4443_lab03;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eecs4443_lab03.placeholder.ContactRepository;

/**
 * A fragment representing a list of Items.
 */
public class ContactFragment extends Fragment implements ContactRecyclerViewAdapter.OnContactClickListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    public ContactFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new ContactRecyclerViewAdapter(ContactRepository.ITEMS, this));
        }
        return view;
    }

    @Override
    public void onContactTap(ContactRepository.Contact item) {
        // tap -> Navigate to detail screen
        Toast.makeText(getContext(), "Tapped on: " + item.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onContactLongPress(ContactRepository.Contact item) {
        // long press -> Navigate to entry screen or show options
        Toast.makeText(getContext(), "Long pressed on: " + item.name, Toast.LENGTH_SHORT).show();
    }
}