package com.example.eecs4443_lab03;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.util.Calendar;

public class ContactEditFragment extends Fragment {
    private static final String ARG_CONTACT_ID = "contact_id";
    private EditText editName;
    private EditText editPhone;
    private EditText editBirthday;
    private EditText editDescription;
    private EditText editNotes;
    private Contact oldContact;
    private Integer contactId;
    private ContactViewModel viewModel;
    public static ContactEditFragment newInstance(int contactId) {
        ContactEditFragment fragment = new ContactEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CONTACT_ID, contactId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_CONTACT_ID)) {
            contactId = args.getInt(ARG_CONTACT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editName = view.findViewById(R.id.add_edit_text_name);
        editPhone = view.findViewById(R.id.add_edit_text_phone);
        editBirthday = view.findViewById(R.id.add_edit_text_birthday);
        editDescription = view.findViewById(R.id.add_edit_text_description);
        editNotes = view.findViewById(R.id.add_edit_text_notes);
        Button saveButton = view.findViewById(R.id.button_add_contact);

        // Load stored data
        viewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        oldContact = viewModel.getContactById(contactId);

        if (oldContact == null) {
            Toast.makeText(getContext(), "Contact not found", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
            return;
        }


        editName.setText(oldContact.getName());
        editPhone.setText(oldContact.getPhoneNumber());
        editBirthday.setText(oldContact.getBirthday().toString());
        editDescription.setText(oldContact.getDescription());
        editNotes.setText(oldContact.getNotes());

        editBirthday.setOnClickListener(v -> showDatePicker());


        saveButton.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            String bday = editBirthday.getText().toString().trim();
            String desc = editDescription.getText().toString().trim();
            String notes = editNotes.getText().toString().trim();

            //validation
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(bday)) {
                Toast.makeText(getContext(), "Please fill required fields", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                Contact updated = new Contact(
                        contactId, name, LocalDate.parse(bday), phone, desc, notes, oldContact.getDateAdded()
                );

                viewModel.editContact(updated);
                Toast.makeText(getContext(), "Contact updated", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();

            } catch (Exception e) {
                Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = year1 + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                    editBirthday.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

}
