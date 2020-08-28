package com.example.scheduledmessenger.data.source.local.dao

import androidx.room.*
import com.example.scheduledmessenger.data.source.local.entity.Event
import com.example.scheduledmessenger.data.source.local.entity.EventWithSmsAndPhoneNumbers
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {
    @Insert
    fun insertEvent(event: Event): Long

    suspend fun insertEventWithTimeStamp(event: Event): Long = insertEvent(event.apply {
        createdAt = System.currentTimeMillis()
        updatedAt = System.currentTimeMillis()
    })

    @Update
    suspend fun updateEvent(event: Event): Int

    suspend fun updateEventWithTimeStamp(event: Event): Int = updateEvent(event.apply {
        updatedAt = System.currentTimeMillis()
    })

    @Query("SELECT * FROM EVENTS WHERE id = :id")
    suspend fun getEventById(id: Int): Event

    @Query("SELECT * FROM EVENTS WHERE timestamp > :timestamp")
    fun getUpcomingEvents(timestamp: Long): List<Event>


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