package com.example.scheduledmessenger.data.contacts.service

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.example.scheduledmessenger.data.contacts.model.Contact
import com.example.scheduledmessenger.data.source.local.entity.PhoneNumber
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

    suspend fun getContactNames(
        phoneNumbers: List<PhoneNumber>
    ): List<String> {
        val names = mutableListOf<String>()

        for (phoneNumber in phoneNumbers) {
            val uri: Uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber.phoneNumber)
            )
            val projection =
                arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
            var contactName = ""
            val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    contactName = cursor.getString(0)

                }
                cursor.close()
            }
            if (contactName.isNotEmpty())
                names.add(contactName)
            else
                names.add(phoneNumber.phoneNumber)
        }
        return names
    }
}