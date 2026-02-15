package com.example.eecs4443_lab03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class ContactFragment extends Fragment implements ContactRecyclerViewAdapter.OnContactClickListener {
    private ContactViewModel viewModel;
    private ContactRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContactRecyclerViewAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        viewModel.contacts.observe(getViewLifecycleOwner(), items -> adapter.updateItems(items));

        RadioGroup storageToggle = view.findViewById(R.id.list_toggle_options);
        RadioButton rbSqlite = view.findViewById(R.id.list_rb_sqlite);
        RadioButton rbPrefs = view.findViewById(R.id.list_rb_shared_prefs);

        viewModel.isSQLiteMode.observe(getViewLifecycleOwner(), isSQLite -> {
            if (isSQLite) rbSqlite.setChecked(true);
            else rbPrefs.setChecked(true);
        });

        storageToggle.setOnCheckedChangeListener((group, checkedId) ->
                viewModel.setStorageMethod(checkedId == R.id.list_rb_sqlite));

        FloatingActionButton addButton = view.findViewById(R.id.floating_button_add);
        addButton.setOnClickListener(v -> navigateTo(new ContactAddFragment()));
    }

    private void navigateTo(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onContactTap(Contact item) {
        navigateTo(ContactDetailsFragment.newInstance(item.getContactID()));
    }

    @Override
    public void onContactLongPress(Contact item) {
        navigateTo(ContactEditFragment.newInstance(item.getContactID()));
    }
}