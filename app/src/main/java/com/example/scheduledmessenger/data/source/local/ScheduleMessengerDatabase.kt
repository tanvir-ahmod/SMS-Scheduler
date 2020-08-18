package com.example.scheduledmessenger.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scheduledmessenger.data.LogModel
import com.example.scheduledmessenger.data.SMS

@Database(entities = [SMS::class, LogModel::class], version = 1, exportSchema = false)
abstract class ScheduleMessengerDatabase : RoomDatabase() {
    abstract fun eventsDao(): SchedulesDao
}
