package com.example.eecs4443_lab03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ContactDetailsFragment extends Fragment {
    private static final String ARG_CONTACT_ID = "contact_id";
    private ContactViewModel viewModel;
    private int contactId;

    public static ContactDetailsFragment newInstance(int contactId) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CONTACT_ID, contactId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) contactId = getArguments().getInt(ARG_CONTACT_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        Button editButton = view.findViewById(R.id.contact_button_edit);
        Button deleteButton = view.findViewById(R.id.contact_button_delete);

        viewModel.contacts.observe(getViewLifecycleOwner(), items -> {
            Contact contact = viewModel.getContactById(contactId);
            if (contact != null) bindContact(view, contact);
        });
        editButton.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, ContactEditFragment.newInstance(contactId))
                        .addToBackStack(null).commit());

        deleteButton.setOnClickListener(v -> {
            viewModel.deleteContact(contactId);
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        });
    }

    private void bindContact(View view, Contact contact) {
        if (contact == null) return;
        TextView name = view.findViewById(R.id.contact_name);
        TextView birthday = view.findViewById(R.id.contact_birthday);
        TextView phoneNumber = view.findViewById(R.id.contact_phone_number);
        TextView description = view.findViewById(R.id.contact_description);
        TextView notes = view.findViewById(R.id.contact_notes);
        name.setText(contact.getName());
        phoneNumber.setText(contact.getPhoneNumber());
        birthday.setText(
                contact.getBirthday().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        description.setText(contact.getDescription());
        notes.setText(contact.getNotes());
    }
}