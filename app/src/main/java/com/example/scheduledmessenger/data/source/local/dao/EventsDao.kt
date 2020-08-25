package com.example.scheduledmessenger.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.scheduledmessenger.data.source.local.entity.Event
import com.example.scheduledmessenger.data.source.local.entity.EventWithSmsAndPhoneNumbers
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp

@Dao
interface EventsDao {
    @Insert
    fun insertEvent(event: Event): Long

    suspend fun insertEventWithTimeStamp(event: Event): Long = insertEvent(event.apply {
        createdAt = System.currentTimeMillis()
        updatedAt = System.currentTimeMillis()
    })


    @Transaction
    @Query("SELECT * FROM EVENTS")
    fun getEventWithSmsAndPhoneNumbers(): Flow<List<EventWithSmsAndPhoneNumbers>>

    @Transaction
    @Query("SELECT COUNT(*) FROM EVENTS WHERE STATUS = :status")
    fun getSMSsWithStatus(status: Int): Flow<Int>

    @Transaction
    @Query("SELECT * FROM EVENTS WHERE timestamp > :timestamp")
    fun getUpcomingSMSEvents(timestamp: Long): Flow<List<EventWithSmsAndPhoneNumbers>>

}