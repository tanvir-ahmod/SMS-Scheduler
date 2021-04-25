package com.example.smsScheduler.data.contacts

import com.example.smsScheduler.data.contacts.model.Contact
import com.example.smsScheduler.data.contacts.service.ContactsServiceImp
import com.example.smsScheduler.data.source.local.entity.PhoneNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(private val contactsServiceImp: ContactsServiceImp) :
    ContactsRepository {

    override fun getContactsByName(name: String): Flow<List<Contact>> = flow {
        emit(contactsServiceImp.getContactsByName(name))
    }

    override suspend fun getContactNamesByPhoneNumbers(phoneNumbers: List<PhoneNumber>): List<String> =
        withContext(Dispatchers.IO) {
            contactsServiceImp.getContactNames(phoneNumbers)
        }
}