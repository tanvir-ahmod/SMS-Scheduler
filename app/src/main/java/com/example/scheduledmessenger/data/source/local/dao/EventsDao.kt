package com.example.scheduledmessenger.data.source.local.dao

import androidx.room.*
import com.example.scheduledmessenger.data.source.local.entity.Event
import com.example.scheduledmessenger.data.source.local.entity.EventWithSmsAndPhoneNumbers
import com.example.scheduledmessenger.utils.Constants
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

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("DELETE FROM EVENTS WHERE id = :id")
    suspend fun deleteEventById(id: Int)

    suspend fun updateEventWithTimeStamp(event: Event): Int = updateEvent(event.apply {
        updatedAt = System.currentTimeMillis()
    })

    @Query("SELECT * FROM EVENTS WHERE id = :id")
    suspend fun getEventById(id: Int): Event

    @Query("SELECT * FROM EVENTS WHERE timestamp > :timestamp ORDER BY TIMESTAMP DESC")
    fun getUpcomingEvents(timestamp: Long): List<Event>

    @Transaction
    @Query("SELECT * FROM EVENTS")
    fun getEventWithSmsAndPhoneNumbers(): Flow<List<EventWithSmsAndPhoneNumbers>>

    @Transaction
    @Query("SELECT COUNT(*) FROM EVENTS WHERE STATUS = :status")
    fun getSMSsWithStatus(status: Int): Flow<Int>

    @Transaction
    @Query("SELECT * FROM EVENTS WHERE timestamp > :timestamp ORDER BY TIMESTAMP DESC")
    fun getUpcomingSMSEvents(timestamp: Long): Flow<List<EventWithSmsAndPhoneNumbers>>

    @Query("SELECT * FROM EVENTS WHERE timestamp < :timestamp AND status = :pendingStatus ORDER BY TIMESTAMP DESC")
    suspend fun getFailedEvents(
        timestamp: Long,
        pendingStatus: Int = Constants.PENDING
    ): List<Event>

}