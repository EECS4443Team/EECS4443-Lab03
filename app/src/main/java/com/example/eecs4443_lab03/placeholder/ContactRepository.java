package com.example.eecs4443_lab03.placeholder;

import android.content.Context;
import android.content.SharedPreferences;

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
        refreshData();
    }
    public void refreshData() {
        ITEMS.clear();
        ITEM_MAP.clear();
        List<Contact> list;
        if (useSQLite) {
            list = dbHelper.getAllContacts();
        } else {
            list = getAllFromSharedPrefs();
        }
        if (list.isEmpty()) {
            initSampleData();
            list = useSQLite ? dbHelper.getAllContacts() : getAllFromSharedPrefs();
        }

        ITEMS.addAll(list);
        for (Contact c : list) {
            ITEM_MAP.put(c.getContactID(), c);
        }
    }

    private void initSampleData() {
        for (int i = 1; i <= 25; i++) {
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
            dbHelper.insertContact(contact);
        } else {
            saveToSharedPrefs(contact);
        }
        refreshData();
    }

    //SharedPrefs Logics
    private void saveToSharedPrefs(Contact contact) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        int count = sharedPrefs.getInt("contact_count", 0);
        String prefix = "contact_" + count + "_";

        editor.putInt(prefix + "id", contact.getContactID());
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