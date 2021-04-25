package com.example.smsScheduler.di

import com.example.smsScheduler.data.contacts.ContactsRepository
import com.example.smsScheduler.data.contacts.ContactsRepositoryImpl
import com.example.smsScheduler.data.source.ScheduleRepository
import com.example.smsScheduler.data.source.ScheduleRepositoryImpl
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