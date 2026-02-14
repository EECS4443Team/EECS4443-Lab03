package com.example.eecs4443_lab03;

import java.time.LocalDate;

/**
 * Simple data model for a Contact with name, birthday, phone number, description, notes and date of addition.
 */

public class Contact {
    private int contactID;
    private String name;
    private LocalDate birthday;
    private String phoneNumber;
    private String description;
    private String notes;
    private LocalDate dateAdded;

    public Contact(
            int contactID,
            String name,
            LocalDate birthday,
            String phoneNumber,
            String description,
            String notes,
            LocalDate dateAdded
    ) {
        this.contactID = contactID;
        this.name = name;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.notes = notes;
        this.dateAdded = dateAdded;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }
}
