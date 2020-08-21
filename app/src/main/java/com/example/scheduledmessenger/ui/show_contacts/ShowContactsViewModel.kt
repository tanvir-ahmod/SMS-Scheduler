package com.example.scheduledmessenger.ui.show_contacts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduledmessenger.base.BaseViewModel
import com.example.scheduledmessenger.data.contacts.ContactsRepository
import com.example.scheduledmessenger.data.contacts.model.Contact
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ShowContactsViewModel @ViewModelInject constructor(private val contactsRepository: ContactsRepository) :
    BaseViewModel() {

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> = _contacts

    init {
        getContacts()
    }

    private fun getContacts() {
        viewModelScope.launch {
            showLoader.value = true
            val contacts = contactsRepository.getContacts()
            contacts.collect { results ->
                _contacts.value = results
            }
            showLoader.value = false
        }

    }

}