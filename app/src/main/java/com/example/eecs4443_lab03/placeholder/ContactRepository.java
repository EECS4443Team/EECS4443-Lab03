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
        android.util.Log.d("STORAGE_CHECK", "useSQLite : " + useSQLite);
        refreshData();
    }

    public void refreshData() {
        ITEMS.clear();
        ITEM_MAP.clear();
        List<Contact> list;
        if (useSQLite) {
            android.util.Log.d("STORAGE_CHECK", "Loading from SQLite");
            list = dbHelper.getAllContacts();
        } else {
            android.util.Log.d("STORAGE_CHECK", "Loading from SharedPreferences ");
            list = getAllFromSharedPrefs();
        }

        if (list.isEmpty()) {
            initSampleData();
            // Re-fetch list after initializing sample data
            list = useSQLite ? dbHelper.getAllContacts() : getAllFromSharedPrefs();
        }

        ITEMS.addAll(list);
        for (Contact c : list) {
            ITEM_MAP.put(c.getContactID(), c);
        }
    }

    private void initSampleData() {
        // Populate sample data into the CURRENTLY selected storage method
        for (int i = 1; i <= 5; i++) {
            Contact sample = createContact(i);
            if (useSQLite) {
                dbHelper.insertContact(sample);
            } else {
                // For SharedPrefs sample, we still use the simple count-based ID
                saveToSharedPrefs(sample, i);
            }
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
            android.util.Log.d("STORAGE_CHECK", "Store to SQLite: " + contact.getName());
            dbHelper.insertContact(contact);
        } else {
            android.util.Log.d("STORAGE_CHECK", "Store to SharedPreferences: " + contact.getName());

            // Logic to prevent ID collisions across both storage sources
            // We find the maximum ID currently stored in BOTH SQLite and SharedPreferences
            List<Contact> allExisting = new ArrayList<>();
            allExisting.addAll(dbHelper.getAllContacts());
            allExisting.addAll(getAllFromSharedPrefs());

            int maxId = 0;
            for (Contact c : allExisting) {
                if (c.getContactID() > maxId) {
                    maxId = c.getContactID();
                }
            }

            // Assign the next available unique ID
            saveToSharedPrefs(contact, maxId + 1);
        }
        refreshData();
    }

    public void editContact(Contact contact) {
        if (useSQLite) {
            android.util.Log.d("STORAGE_CHECK", "Update in SQLite: " + contact.getName());
            dbHelper.updateContact(contact);
        } else {
            android.util.Log.d("STORAGE_CHECK", "Update in SharedPreferences: " + contact.getName());
            updateSharedPrefsContact(contact);
        }
        refreshData();
    }

    public void deleteContact(int contactId) {
        if (useSQLite) {
            android.util.Log.d("STORAGE_CHECK", "Delete from SQLite: " + contactId);
            dbHelper.deleteContact(contactId);
        } else {
            android.util.Log.d("STORAGE_CHECK", "Delete from SharedPreferences: " + contactId);
            deleteFromSharedPrefs(contactId);
        }
        refreshData();
    }

    //SharedPrefs Logics
    private void saveToSharedPrefs(Contact contact, int newId) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        int count = sharedPrefs.getInt("contact_count", 0);
        String prefix = "contact_" + count + "_";

        // Save the explicitly generated unique ID
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

    private void updateSharedPrefsContact(Contact contact) {
        int count = sharedPrefs.getInt("contact_count", 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        for (int i = 0; i < count; i++) {
            String prefix = "contact_" + i + "_";
            int id = sharedPrefs.getInt(prefix + "id", -1);
            if (id == contact.getContactID()) {
                editor.putString(prefix + "name", contact.getName());
                editor.putString(prefix + "phone", contact.getPhoneNumber());
                editor.putString(prefix + "bday", contact.getBirthday().toString());
                editor.putString(prefix + "desc", contact.getDescription());
                editor.putString(prefix + "notes", contact.getNotes());
                editor.putString(prefix + "date", contact.getDateAdded().toString());
                editor.apply();
                return;
            }
        }
    }

    private void deleteFromSharedPrefs(int contactId) {
        List<Contact> existing = getAllFromSharedPrefs();
        List<Contact> kept = new ArrayList<>();

        for (Contact contact : existing) {
            if (contact.getContactID() != contactId) {
                kept.add(contact);
            }
        }

        int count = sharedPrefs.getInt("contact_count", 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        for (int i = 0; i < count; i++) {
            String prefix = "contact_" + i + "_";
            editor.remove(prefix + "id");
            editor.remove(prefix + "name");
            editor.remove(prefix + "phone");
            editor.remove(prefix + "bday");
            editor.remove(prefix + "desc");
            editor.remove(prefix + "notes");
            editor.remove(prefix + "date");
        }
        editor.putInt("contact_count", 0);
        editor.apply();

        for (Contact contact : kept) {
            saveToSharedPrefs(contact, contact.getContactID());
        }
    }
}
