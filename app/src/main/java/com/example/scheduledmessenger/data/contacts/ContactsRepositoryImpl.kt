package com.example.scheduledmessenger.data.contacts

import com.example.scheduledmessenger.data.contacts.model.Contact
import com.example.scheduledmessenger.data.contacts.service.ContactsServiceImp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(private val contactsServiceImp: ContactsServiceImp) :
    ContactsRepository {
    override fun getContacts(): Flow<List<Contact>> = flow {
        emit(contactsServiceImp.getContacts())
    }
}