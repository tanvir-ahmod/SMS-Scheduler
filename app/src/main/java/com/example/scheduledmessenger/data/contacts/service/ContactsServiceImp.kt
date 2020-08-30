package com.example.scheduledmessenger.data.contacts.service

import android.content.Context
import android.provider.ContactsContract
import com.example.scheduledmessenger.data.contacts.model.Contact
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContactsServiceImp @Inject constructor(@ApplicationContext private val context: Context) {

    fun getContactsByName(name: String): List<Contact> {
        val selection = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?"
        val contacts = arrayListOf<Contact>()
        val selectionArgs = arrayOf("%$name%")
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, selection, selectionArgs,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val displayName =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNo =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val photoUri =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                contacts.add(Contact(displayName, phoneNo, photoUri))
            }
        }
        cursor?.close()
        return contacts
    }
}