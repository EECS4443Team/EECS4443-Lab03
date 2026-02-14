package com.example.eecs4443_lab03;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContactDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ContactManager.db";
    private static final int DATABASE_VERSION = 1;

    // Table and Column names
    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONENumber = "phoneNumber";
    public static final String COLUMN_BDAY = "birthday";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_NOTES = "notes";

    public ContactDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PHONENumber + " TEXT,"
                + COLUMN_BDAY + " TEXT,"
                + COLUMN_DESC + " TEXT,"
                + COLUMN_NOTES + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // CREATE: Add a new contact
    public long insertContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONENumber, contact.getPhoneNumber());
        values.put(COLUMN_BDAY, contact.getBirthday().toString()); // ISO-8601 format
        values.put(COLUMN_DESC, contact.getDescription());
        values.put(COLUMN_NOTES, contact.getNotes());

        return db.insert(TABLE_CONTACTS, null, values);
    }

    // READ: Get all contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);

        if (cursor.moveToFirst()) {
            do {
                contactList.add(new Contact(
                        cursor.getString(1), // name
                        LocalDate.parse(cursor.getString(3)), // birthday
                        cursor.getString(2), // phone
                        cursor.getString(4), // description
                        cursor.getString(5), // notes
                        LocalDate.now()      // dateOfAddition (simplified)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    // Add these to your ContactDBHelper class
    public int deleteContact(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Returns the number of rows affected
        return db.delete(TABLE_CONTACTS, COLUMN_NAME + " = ?", new String[]{name});
    }

    public int updateContact(Contact contact, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONENumber, contact.getPhoneNumber());
        values.put(COLUMN_NOTES, contact.getNotes());
        // Add other fields as needed...

        return db.update(TABLE_CONTACTS, values, COLUMN_NAME + " = ?", new String[]{oldName});
    }
}