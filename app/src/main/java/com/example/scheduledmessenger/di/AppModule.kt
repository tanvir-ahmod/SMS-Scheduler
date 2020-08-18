package com.example.scheduledmessenger.di

import android.content.Context
import androidx.room.Room
import com.example.scheduledmessenger.data.source.local.ScheduleMessengerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
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

}