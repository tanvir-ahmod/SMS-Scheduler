package com.example.scheduledmessenger.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.scheduledmessenger.data.source.local.db_models.SMS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Dao
interface SmsDao {

    @Query("SELECT COUNT(*) FROM SMSs")
    fun getTotalSMSs(): Flow<Int>

    @Insert
    fun insertSingleSMS(sms: SMS): Long

    suspend fun insertSmsWithTimeStamp(sms: SMS) : Long = insertSingleSMS(sms.apply {
        createdAt = System.currentTimeMillis()
        updatedAt = System.currentTimeMillis()
    })


}