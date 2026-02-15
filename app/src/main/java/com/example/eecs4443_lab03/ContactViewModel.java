package com.example.eecs4443_lab03;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.eecs4443_lab03.placeholder.ContactRepository;
import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private final ContactRepository repository;

    private final MutableLiveData<List<Contact>> _contacts = new MutableLiveData<>();
    public final LiveData<List<Contact>> contacts = _contacts;

    private final MutableLiveData<Boolean> _isSQLiteMode = new MutableLiveData<>();
    public final LiveData<Boolean> isSQLiteMode = _isSQLiteMode;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        repository = ContactRepository.getInstance(application);
        _isSQLiteMode.setValue(repository.isUsingSQLite());
        refresh();
    }

    public void refresh() {
        repository.refreshData();
        _contacts.setValue(ContactRepository.ITEMS);
    }

    public void setStorageMethod(boolean useSQLite) {
        repository.setStorageMethod(useSQLite);
        _isSQLiteMode.setValue(useSQLite);
        refresh();
    }

    public void addContact(Contact contact) {
        repository.addContact(contact);
        refresh();
    }

    public void editContact(Contact contact) {
        repository.editContact(contact);
        refresh();
    }

    public void deleteContact(int id) {
        repository.deleteContact(id);
        refresh();
    }

    public Contact getContactById(int id) {
        return ContactRepository.ITEM_MAP.get(id);
    }
}