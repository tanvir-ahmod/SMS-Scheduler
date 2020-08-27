package com.example.scheduledmessenger.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.scheduledmessenger.data.source.local.entity.SMS
import com.example.scheduledmessenger.data.source.local.entity.SmsAndPhoneNumbers
import kotlinx.coroutines.flow.Flow

@Dao
interface SmsDao {

    @Query("SELECT COUNT(*) FROM SMSs")
    fun getTotalSMSs(): Flow<Int>

    @Insert
    fun insertSingleSMS(sms: SMS): Long

    suspend fun insertSmsWithTimeStamp(sms: SMS): Long = insertSingleSMS(sms.apply {
        createdAt = System.currentTimeMillis()
        updatedAt = System.currentTimeMillis()
    })

    @Transaction
    @Query("SELECT * FROM SMSs")
    fun getSmsAndPhoneNumbers(): Flow<List<SmsAndPhoneNumbers>>

    @Transaction
    @Query("SELECT * FROM SMSs WHERE event_id = :id")
    suspend fun getSmsAndPhoneNumbersWithEventId(id : Int): SmsAndPhoneNumbers



}