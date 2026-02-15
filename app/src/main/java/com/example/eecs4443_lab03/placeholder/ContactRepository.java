package com.example.eecs4443_lab03.placeholder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.eecs4443_lab03.Contact;
import com.example.eecs4443_lab03.ContactDBHelper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactRepository {
    private static ContactRepository instance;
    public static final List<Contact> ITEMS = new ArrayList<>();
    public static final Map<Integer, Contact> ITEM_MAP = new HashMap<>();
    private final Context context;
    private final SharedPreferences sharedPrefs;
    private boolean useSQLite = true;
    private final ContactDBHelper dbHelper;

    public ContactRepository(Context context) {
        this.context = context.getApplicationContext();
        this.dbHelper = new ContactDBHelper(this.context);
        this.sharedPrefs = this.context.getSharedPreferences("ContactPrefs", Context.MODE_PRIVATE);
        // update whe instantiation
        refreshData();
    }

    public static ContactRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ContactRepository(context);
        }
        return instance;
    }

    public void setStorageMethod(boolean useSQLite) {
        this.useSQLite = useSQLite;
        Log.d("STORAGE_CHECK", "Storage method changed -> useSQLite: " + useSQLite);
        // We refresh to ensure the UI stays updated even if the view logic changes
        refreshData();
    }

    public void refreshData() {
        ITEMS.clear();
        ITEM_MAP.clear();

        // Fetch data from both sources to create a unified list
        List<Contact> sqliteList = dbHelper.getAllContacts();
        List<Contact> sharedList = getAllFromSharedPrefs();

        // Combine both lists into the main ITEMS list
        ITEMS.addAll(sqliteList);
        ITEMS.addAll(sharedList);

        // Map items by ID for easy access (e.g., for details view)
        for (Contact c : ITEMS) {
            ITEM_MAP.put(c.getContactID(), c);
        }

        // If no data exists anywhere, initialize with sample data
        if (ITEMS.isEmpty()) {
            initSampleData();
            refreshData(); // Re-run to load the newly created sample data
        }
    }

    private void initSampleData() {
        // Initialize with a few samples to populate the UI initially
        for (int i = 1; i <= 5; i++) {
            dbHelper.insertContact(createContact(i));
        }
    }

    private Contact createContact(int position) {
        LocalDate birthday = LocalDate.of(1990 + (position % 10), 5, (position % 28) + 1);
        return new Contact(
                position,
                "Contact " + position,
                birthday,
                "555-01" + String.format("%02d", position),
                "Details about Contact: " + position,
                "Note for " + position,
                LocalDate.now()
        );
    }

    public void addContact(Contact contact) {
        if (useSQLite) {
            Log.d("STORAGE_CHECK", "Attempting to save to SQLite: " + contact.getName());
            dbHelper.insertContact(contact);
        } else {
            Log.d("STORAGE_CHECK", "Attempting to save to SharedPreferences: " + contact.getName());

            // Generate a unique ID by finding the maximum ID currently in the combined list
            int maxId = 0;
            for (Contact c : ITEMS) {
                if (c.getContactID() > maxId) {
                    maxId = c.getContactID();
                }
            }
            // Save with the next available ID to prevent collisions
            saveToSharedPrefs(contact, maxId + 1);
        }
        refreshData();
    }

    // SharedPrefs Logics
    private void saveToSharedPrefs(Contact contact, int newId) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        int count = sharedPrefs.getInt("contact_count", 0);
        String prefix = "contact_" + count + "_";

        // Use the calculated unique ID instead of the temporary 0 from the form
        editor.putInt(prefix + "id", newId);
        editor.putString(prefix + "name", contact.getName());
        editor.putString(prefix + "phone", contact.getPhoneNumber());
        editor.putString(prefix + "bday", contact.getBirthday().toString());
        editor.putString(prefix + "desc", contact.getDescription());
        editor.putString(prefix + "notes", contact.getNotes());
        editor.putString(prefix + "date", contact.getDateAdded().toString());

        editor.putInt("contact_count", count + 1);
        editor.apply();
    }

    private List<Contact> getAllFromSharedPrefs() {
        List<Contact> list = new ArrayList<>();
        int count = sharedPrefs.getInt("contact_count", 0);

        for (int i = 0; i < count; i++) {
            String prefix = "contact_" + i + "_";
            int id = sharedPrefs.getInt(prefix + "id", -1);
            if (id == -1) continue;

            list.add(new Contact(
                    id,
                    sharedPrefs.getString(prefix + "name", ""),
                    LocalDate.parse(sharedPrefs.getString(prefix + "bday", "2000-01-01")),
                    sharedPrefs.getString(prefix + "phone", ""),
                    sharedPrefs.getString(prefix + "desc", ""),
                    sharedPrefs.getString(prefix + "notes", ""),
                    LocalDate.parse(sharedPrefs.getString(prefix + "date", LocalDate.now().toString()))
            ));
        }
        return list;
    }
}