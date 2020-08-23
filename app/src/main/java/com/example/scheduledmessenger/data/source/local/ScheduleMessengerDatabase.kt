package com.example.scheduledmessenger.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scheduledmessenger.data.source.local.dao.EventsDao
import com.example.scheduledmessenger.data.source.local.dao.EventLogsDao
import com.example.scheduledmessenger.data.source.local.dao.PhoneNumbersDao
import com.example.scheduledmessenger.data.source.local.dao.SmsDao
import com.example.scheduledmessenger.data.source.local.db_models.Event
import com.example.scheduledmessenger.data.source.local.db_models.EventLog
import com.example.scheduledmessenger.data.source.local.db_models.PhoneNumber
import com.example.scheduledmessenger.data.source.local.db_models.SMS

@Database(
    entities = [Event::class, SMS::class, PhoneNumber::class, EventLog::class],
    version = 1,
    exportSchema = false
)
abstract class ScheduleMessengerDatabase : RoomDatabase() {
    abstract fun eventsDao(): EventsDao

    abstract fun smsDao(): SmsDao

    abstract fun phoneNumbersDao(): PhoneNumbersDao

    abstract fun eventLogsDao(): EventLogsDao
}
