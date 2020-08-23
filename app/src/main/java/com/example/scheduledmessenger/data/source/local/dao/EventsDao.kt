package com.example.scheduledmessenger.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.scheduledmessenger.data.source.local.db_models.Event

@Dao
interface EventsDao {
    @Insert
    fun insertEvent(event: Event): Long

   suspend fun insertEventWithTimeStamp(event: Event) : Long = insertEvent(event.apply {
        createdAt = System.currentTimeMillis()
        updatedAt = System.currentTimeMillis()
    })
}