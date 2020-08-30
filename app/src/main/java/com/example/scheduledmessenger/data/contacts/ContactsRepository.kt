package com.example.scheduledmessenger.data.contacts

import com.example.scheduledmessenger.data.contacts.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getContactsByName(name : String) : Flow<List<Contact>>
}