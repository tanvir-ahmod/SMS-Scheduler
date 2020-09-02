package com.example.scheduledmessenger.data.contacts

import com.example.scheduledmessenger.data.contacts.model.Contact
import com.example.scheduledmessenger.data.source.local.entity.EventWithSmsAndPhoneNumbers
import com.example.scheduledmessenger.data.source.local.entity.PhoneNumber
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getContactsByName(name: String): Flow<List<Contact>>
    suspend fun getContactNamesByPhoneNumbers(phoneNumbers: List<PhoneNumber>): List<String>
}