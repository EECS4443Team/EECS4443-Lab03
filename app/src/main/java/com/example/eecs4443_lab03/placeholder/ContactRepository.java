package com.example.eecs4443_lab03.placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return new Contact(
                String.valueOf(position),
                "Contact " + position,
                "555-01" + String.format("%02d", position), // Example phone number
                "contact." + position + "@example.com", // Example email
                makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Contact: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
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
        public final String details;

        public Contact(String id, String name, String phoneNumber, String email, String details) {
            this.id = id;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.details = details;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}