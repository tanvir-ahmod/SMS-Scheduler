package com.example.scheduledmessenger.data.contacts.service

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.example.scheduledmessenger.data.contacts.model.Contact
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContactsServiceImp @Inject constructor(@ApplicationContext private val context: Context) {

    fun getContacts(): List<Contact> {
        val contacts = arrayListOf<Contact>()
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNo =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val photoUri =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                Log.e(
                    "contact",
                    "getAllContacts: $name $phoneNo $photoUri"
                )
                contacts.add(Contact(name, phoneNo, photoUri))
            }
        }
        cursor?.close()
        return contacts
    }
}