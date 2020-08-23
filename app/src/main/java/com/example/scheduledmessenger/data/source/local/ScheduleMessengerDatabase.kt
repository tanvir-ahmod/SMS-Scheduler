package com.example.scheduledmessenger.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scheduledmessenger.data.source.local.dao.EventsDao
import com.example.scheduledmessenger.data.source.local.dao.SmsDao
import com.example.scheduledmessenger.data.source.local.db_models.Event
import com.example.scheduledmessenger.data.source.local.db_models.Log
import com.example.scheduledmessenger.data.source.local.db_models.PhoneNumbers
import com.example.scheduledmessenger.data.source.local.db_models.SMS

@Database(
    entities = [Event::class, SMS::class, PhoneNumbers::class, Log::class],
    version = 1,
    exportSchema = false
)
abstract class ScheduleMessengerDatabase : RoomDatabase() {
    abstract fun eventsDao(): EventsDao

    abstract fun smsDao(): SmsDao
}
