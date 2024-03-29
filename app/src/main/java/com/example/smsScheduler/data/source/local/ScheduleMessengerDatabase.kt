package com.example.smsScheduler.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smsScheduler.data.source.local.dao.EventsDao
import com.example.smsScheduler.data.source.local.dao.EventLogsDao
import com.example.smsScheduler.data.source.local.dao.PhoneNumbersDao
import com.example.smsScheduler.data.source.local.dao.SmsDao
import com.example.smsScheduler.data.source.local.entity.Event
import com.example.smsScheduler.data.source.local.entity.EventLog
import com.example.smsScheduler.data.source.local.entity.PhoneNumber
import com.example.smsScheduler.data.source.local.entity.SMS

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
