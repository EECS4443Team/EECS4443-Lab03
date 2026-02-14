package com.example.eecs4443_lab03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eecs4443_lab03.placeholder.ContactRepository;

public class ContactDetailsFragment extends Fragment {

    private static final String ARG_CONTACT_ID = "contact_id";

    private ContactRepository.Contact mContact;

    public static ContactDetailsFragment newInstance(String contactId) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTACT_ID, contactId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String contactId = getArguments().getString(ARG_CONTACT_ID);
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

        if (mContact != null) {
            // Find all the TextViews in the layout
            TextView name = view.findViewById(R.id.contact_name);
            TextView birthday = view.findViewById(R.id.contact_birthday);
            TextView phoneNumber = view.findViewById(R.id.contact_phone_number);
            TextView description = view.findViewById(R.id.contact_description);
            TextView notes = view.findViewById(R.id.contact_notes);
            TextView dateAdded = view.findViewById(R.id.contact_date_added);

            // Set the text for each TextView
            name.setText(mContact.name);
            birthday.setText(mContact.birthday);
            phoneNumber.setText(mContact.phoneNumber);
            description.setText(mContact.description);
            notes.setText(mContact.notes);
            dateAdded.setText(mContact.dateAdded);
        }
    }
}