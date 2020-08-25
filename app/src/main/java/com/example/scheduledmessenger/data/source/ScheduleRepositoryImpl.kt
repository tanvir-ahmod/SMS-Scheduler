package com.example.scheduledmessenger.data.source

import com.example.scheduledmessenger.data.source.local.ScheduleLocalDataSource
import com.example.scheduledmessenger.data.source.local.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val eventsDataSource: ScheduleLocalDataSource
) :
    ScheduleRepository {
    override fun getTotalSMS(): Flow<Int> = eventsDataSource.getTotalSms()

    override fun getSMSsWithStatus(status: Int): Flow<Int> =
        eventsDataSource.getSMSsWithStatus(status)

    override suspend fun insertSMS(sms: SMS): Long =
        withContext(Dispatchers.IO) { eventsDataSource.insertSMS(sms) }

    override suspend fun insertEvent(event: Event): Long =
        withContext(Dispatchers.IO) { eventsDataSource.insertEvent(event) }

    override suspend fun insertPhoneNumber(phoneNumber: PhoneNumber): Long =
        withContext(Dispatchers.IO) { eventsDataSource.insertPhoneNumber(phoneNumber) }

    override suspend fun insertPhoneNumbers(phoneNumbers: List<PhoneNumber>) =
        withContext(Dispatchers.IO) { eventsDataSource.insertPhoneNumbers(phoneNumbers) }

    override suspend fun insertLog(eventLog: EventLog): Long =
        withContext(Dispatchers.IO) { eventsDataSource.insertLog(eventLog) }

    override fun getSmsAndPhoneNumbers(): Flow<List<SmsAndPhoneNumbers>> =
        eventsDataSource.getSmsAndPhoneNumbers()

    override fun getEventWithSmsAndPhoneNumbers(): Flow<List<EventWithSmsAndPhoneNumbers>> =
        eventsDataSource.getEventWithSmsAndPhoneNumbers()

    override fun getUpcomingSMSEvents(timestamp: Long): Flow<List<EventWithSmsAndPhoneNumbers>> =
        eventsDataSource.getUpcomingSMSEvents(timestamp)

    override fun getAllLogs(): Flow<List<EventLog>> = eventsDataSource.getAllLogs()
    override suspend fun updateEvent(event: Event): Int =
        withContext(Dispatchers.IO) { eventsDataSource.updateEvent(event) }

    override fun getEventById(id: Int): Event  = eventsDataSource.getEventById(id)

}