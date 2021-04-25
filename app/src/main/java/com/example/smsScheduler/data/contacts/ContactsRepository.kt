package com.example.smsScheduler.data.contacts

import com.example.smsScheduler.data.contacts.model.Contact
import com.example.smsScheduler.data.source.local.entity.PhoneNumber
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getContactsByName(name: String): Flow<List<Contact>>
    suspend fun getContactNamesByPhoneNumbers(phoneNumbers: List<PhoneNumber>): List<String>
}