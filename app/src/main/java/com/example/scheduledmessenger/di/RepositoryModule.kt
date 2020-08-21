package com.example.scheduledmessenger.di

import com.example.scheduledmessenger.data.contacts.ContactsRepository
import com.example.scheduledmessenger.data.contacts.ContactsRepositoryImpl
import com.example.scheduledmessenger.data.source.ScheduleRepository
import com.example.scheduledmessenger.data.source.ScheduleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun providesScheduleRepository( scheduleRepository: ScheduleRepositoryImpl): ScheduleRepository

    @Binds
    abstract fun providesContactsRepository( contactsRepository: ContactsRepositoryImpl): ContactsRepository
}