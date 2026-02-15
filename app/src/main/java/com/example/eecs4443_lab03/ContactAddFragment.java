package com.example.eecs4443_lab03;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import java.time.LocalDate;
import java.util.Calendar;

public class ContactAddFragment extends Fragment {

    private EditText etName, etPhone, etBirthday, etDescription, etNotes;
    private RadioGroup rgStorage;
    private ContactViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.add_edit_text_name);
        etPhone = view.findViewById(R.id.add_edit_text_phone);
        etBirthday = view.findViewById(R.id.add_edit_text_birthday);
        etDescription = view.findViewById(R.id.add_edit_text_description);
        etNotes = view.findViewById(R.id.add_edit_text_notes);
        rgStorage = view.findViewById(R.id.add_toggle_options);
        Button btnAdd = view.findViewById(R.id.button_add_contact);

        viewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        // toggle storage method
        viewModel.isSQLiteMode.observe(getViewLifecycleOwner(), isSQLite -> {
            if (isSQLite) {
                rgStorage.check(R.id.rbSQLite);
            } else {
                rgStorage.check(R.id.rbSharedPrefs);
            }
        });

        etBirthday.setOnClickListener(v -> showDatePicker());
        // Toggle storage method
        rgStorage.setOnCheckedChangeListener((group, checkedId) -> {
            boolean useSQLite = (checkedId == R.id.rbSQLite);
            viewModel.setStorageMethod(useSQLite);
            String mode = useSQLite ? "SQLite" : "SharedPreferences";
            Toast.makeText(getContext(), "Storage Mode: " + mode, Toast.LENGTH_SHORT).show();
        });


        btnAdd.setOnClickListener(v -> attemptSaveContact());
    }

    private void attemptSaveContact() {

        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String bday = etBirthday.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();


        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            LocalDate birthday = TextUtils.isEmpty(bday) ? LocalDate.now() : LocalDate.parse(bday);
            Contact newContact = new Contact(
                    0,
                    name,
                    birthday,
                    phone,
                    desc,
                    notes,
                    LocalDate.now()
            );

            viewModel.addContact(newContact);

            Toast.makeText(getContext(), "Saved successfully!", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack(); // get back to list

        } catch (Exception e) {
            Toast.makeText(getContext(), "Invalid date format. Please use YYYY-MM-DD", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = year1 + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                    etBirthday.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

}
