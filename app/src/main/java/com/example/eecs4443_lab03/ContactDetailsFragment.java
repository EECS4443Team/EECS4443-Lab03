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

import com.example.eecs4443_lab03.placeholder.ContactRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ContactDetailsFragment extends Fragment {

    private static final String ARG_CONTACT_ID = "contact_id";

    private Contact mContact;
    private Integer contactId;

    public static ContactDetailsFragment newInstance(int contactId) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CONTACT_ID, contactId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_CONTACT_ID)) {
            contactId = args.getInt(ARG_CONTACT_ID);
            mContact = ContactRepository.ITEM_MAP.get(contactId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button editButton = view.findViewById(R.id.contact_button_edit);
        Button deleteButton = view.findViewById(R.id.contact_button_delete);
        ContactRepository repository = ContactRepository.getInstance(requireContext());

        editButton.setOnClickListener(v -> {
            if (contactId == null) return;
            ContactEditFragment editFragment = ContactEditFragment.newInstance(contactId);
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, editFragment)
                    .addToBackStack(null)
                    .commit();
        });

        deleteButton.setOnClickListener(v -> {
            if (contactId == null) return;
            repository.deleteContact(contactId);
            Toast.makeText(getContext(), "Contact deleted", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        });

        bindContact(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        ContactRepository repository = ContactRepository.getInstance(requireContext());
        repository.refreshData();
        if (contactId != null) {
            mContact = ContactRepository.ITEM_MAP.get(contactId);
        }
        View view = getView();
        if (view != null) {
            bindContact(view);
        }
    }

    private void bindContact(@NonNull View view) {
        if (mContact == null) return;
        TextView name = view.findViewById(R.id.contact_name);
        TextView birthday = view.findViewById(R.id.contact_birthday);
        TextView phoneNumber = view.findViewById(R.id.contact_phone_number);
        TextView description = view.findViewById(R.id.contact_description);
        TextView notes = view.findViewById(R.id.contact_notes);
        TextView dateAdded = view.findViewById(R.id.contact_date_added);

        name.setText(mContact.getName());
        birthday.setText(formateData(mContact.getBirthday()));
        phoneNumber.setText(mContact.getPhoneNumber());
        description.setText(mContact.getDescription());
        notes.setText(mContact.getNotes());
        dateAdded.setText(formateData(mContact.getDateAdded()));
    }
    private String formateData(LocalDate date) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }
}
