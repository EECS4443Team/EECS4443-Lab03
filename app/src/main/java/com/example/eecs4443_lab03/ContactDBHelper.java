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
    // Increment version if you previously ran the app with a different schema
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONENumber = "phoneNumber";
    public static final String COLUMN_BDAY = "birthday";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_DATE_ADDED = "date_added";

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
                + COLUMN_NOTES + " TEXT,"
                + COLUMN_DATE_ADDED + " TEXT DEFAULT (date('now'))" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // CREATE: The ID is handled by SQLite; we don't pass it in the ContentValues
    public long insertContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONENumber, contact.getPhoneNumber());
        values.put(COLUMN_BDAY, contact.getBirthday().toString());
        values.put(COLUMN_DESC, contact.getDescription());
        values.put(COLUMN_NOTES, contact.getNotes());
        // date_added is handled by DEFAULT (date('now'))

        return db.insert(TABLE_CONTACTS, null, values);
    }

    // READ: Get all contacts using Column Names instead of hardcoded indices
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);

        if (cursor.moveToFirst()) {
            do {
                // Using getColumnIndex makes your code "future-proof"
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONENumber));
                String bdayStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BDAY));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC));
                String notes = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES));
                String dateAddedStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_ADDED));

                contactList.add(new Contact(
                        id,
                        name,
                        LocalDate.parse(bdayStr),
                        phone,
                        desc,
                        notes,
                        LocalDate.parse(dateAddedStr)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    // DELETE: Better to delete by ID than by Name (names aren't unique!)
    public int deleteContact(int contactID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?", new String[]{String.valueOf(contactID)});
    }

    // UPDATE: Use the ID to find the specific record
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONENumber, contact.getPhoneNumber());
        values.put(COLUMN_BDAY, contact.getBirthday().toString());
        values.put(COLUMN_DESC, contact.getDescription());
        values.put(COLUMN_NOTES, contact.getNotes());

        return db.update(TABLE_CONTACTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getContactID())});
    }
}