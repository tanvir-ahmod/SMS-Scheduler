package com.example.smsScheduler.di

import android.content.Context
import androidx.room.Room
import com.example.smsScheduler.data.source.local.ScheduleMessengerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): ScheduleMessengerDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ScheduleMessengerDatabase::class.java,
            "MessengerDatabase.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideEventsDao(database: ScheduleMessengerDatabase) = database.eventsDao()

    @Singleton
    @Provides
    fun provideSmsDao(database: ScheduleMessengerDatabase) = database.smsDao()

    @Singleton
    @Provides
    fun providePhoneNumbersDao(database: ScheduleMessengerDatabase) = database.phoneNumbersDao()

    @Singleton
    @Provides
    fun provideLogsDao(database: ScheduleMessengerDatabase) = database.eventLogsDao()

}