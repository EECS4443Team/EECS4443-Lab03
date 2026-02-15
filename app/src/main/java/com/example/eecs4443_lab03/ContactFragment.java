package com.example.eecs4443_lab03;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eecs4443_lab03.placeholder.ContactRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    public void onResume() {
        super.onResume();
        // Update the list, if edited
        ContactRepository repository = ContactRepository.getInstance(getContext());
        repository.refreshData();

        // Notify the adapter that the data has changed
        if (getView() instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) getView();
            if (recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        if (recyclerView != null) {
            Context context = recyclerView.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new ContactRecyclerViewAdapter(ContactRepository.ITEMS, this));
        }

        FloatingActionButton addButton = view.findViewById(R.id.floating_button_add);
        if (addButton != null) {
            addButton.setOnClickListener(v -> {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ContactAddFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
        }
        return view;
    }

    @Override
    public void onContactTap(Contact item) {
        // Create a new instance of the details fragment
        ContactDetailsFragment detailsFragment = ContactDetailsFragment.newInstance(item.getContactID());

        // Replace the current fragment with the details fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, detailsFragment);
        fragmentTransaction.addToBackStack(null); // Add this transaction to the back stack
        fragmentTransaction.commit();
    }

    @Override
    public void onContactLongPress(Contact item) {
        // TODO: Handle contact long press -> Navigate to entry screen or show options
        Toast.makeText(getContext(), "Long pressed on: " + item.getName(), Toast.LENGTH_SHORT).show();
    }
}
