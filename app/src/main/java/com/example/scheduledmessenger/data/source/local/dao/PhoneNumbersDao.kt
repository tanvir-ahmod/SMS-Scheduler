package com.example.scheduledmessenger.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.scheduledmessenger.data.source.local.entity.PhoneNumber

@Dao
interface PhoneNumbersDao {

    @Insert
    fun insertSinglePhoneNumber(phoneNumber: PhoneNumber): Long

    @Insert
    suspend fun insertSinglePhoneNumbers(phoneNumbers: List<PhoneNumber>)

    suspend fun insertSmsWithTimeStamp(phoneNumber: PhoneNumber) : Long = insertSinglePhoneNumber(phoneNumber.apply {
        createdAt = System.currentTimeMillis()
        updatedAt = System.currentTimeMillis()
    })


}