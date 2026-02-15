package com.example.eecs4443_lab03;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private RecyclerView recyclerView;
    private ContactRecyclerViewAdapter adapter;
    private ContactRepository repository;

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
        // Update the list when returning from details/edit/add
        repository = ContactRepository.getInstance(requireContext());
        repository.refreshData();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        repository = ContactRepository.getInstance(requireContext());

        recyclerView = view.findViewById(R.id.list);
        if (recyclerView != null) {
            Context context = recyclerView.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new ContactRecyclerViewAdapter(ContactRepository.ITEMS, this);
            recyclerView.setAdapter(adapter);
        }

        RadioGroup storageToggle = view.findViewById(R.id.list_toggle_options);
        RadioButton toggleSharedPrefs = view.findViewById(R.id.list_rb_shared_prefs);
        RadioButton toggleSQLite = view.findViewById(R.id.list_rb_sqlite);
        if (repository.isUsingSQLite()) {
            toggleSQLite.setChecked(true);
        } else {
            toggleSharedPrefs.setChecked(true);
        }
        storageToggle.setOnCheckedChangeListener((group, checkedId) -> {
            boolean useSQLite = checkedId == R.id.list_rb_sqlite;
            repository.setStorageMethod(useSQLite);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        });

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
