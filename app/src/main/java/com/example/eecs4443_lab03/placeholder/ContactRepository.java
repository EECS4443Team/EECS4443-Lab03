package com.example.eecs4443_lab03.placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ContactRepository {

    public static final List<Contact> ITEMS = new ArrayList<>();
    public static final Map<String, Contact> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createContact(i));
        }
    }

    private static void addItem(Contact item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Contact createContact(int position) {
        String dateAdded = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return new Contact(
                String.valueOf(position),
                "Contact " + position, // name
                "555-01" + String.format("%02d", position), // phoneNumber
                "contact." + position + "@example.com",     // email
                makeDetails(position), // description
                "199" + (position % 10) + "-05-" + String.format("%02d", position), // birthday
                "This is a note for contact " + position + ".", // notes
                dateAdded // date of addition
        );
    }
    
    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Contact: ").append(position);
        builder.append("\nMore details information here.");
        return builder.toString();
    }

    /**
     * A contact item representing a piece of content.
     */
    public static class Contact {
        public final String id;
        public final String name;
        public final String phoneNumber;
        public final String email;
        public final String description;
        public final String birthday;
        public final String notes;
        public final String dateAdded;


        public Contact(String id, String name, String phoneNumber, String email, String description, String birthday, String notes, String dateAdded) {
            this.id = id;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.description = description;
            this.birthday = birthday;
            this.notes = notes;
            this.dateAdded = dateAdded;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}