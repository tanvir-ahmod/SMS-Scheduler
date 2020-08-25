package com.example.scheduledmessenger.data.source.local

import com.example.scheduledmessenger.data.source.local.dao.EventsDao
import com.example.scheduledmessenger.data.source.local.dao.EventLogsDao
import com.example.scheduledmessenger.data.source.local.dao.PhoneNumbersDao
import com.example.scheduledmessenger.data.source.local.dao.SmsDao
import com.example.scheduledmessenger.data.source.local.entity.Event
import com.example.scheduledmessenger.data.source.local.entity.EventLog
import com.example.scheduledmessenger.data.source.local.entity.PhoneNumber
import com.example.scheduledmessenger.data.source.local.entity.SMS
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleLocalDataSource @Inject constructor(
    private val eventsDao: EventsDao,
    private val smsDao: SmsDao,
    private val phoneNumbersDao: PhoneNumbersDao,
    private val eventLogsDao: EventLogsDao
) {
    fun getTotalSms(): Flow<Int> = smsDao.getTotalSMSs()

    fun getSMSsWithStatus(status: Int): Flow<Int> = eventsDao.getSMSsWithStatus(status)

    suspend fun insertSMS(sms: SMS): Long = smsDao.insertSmsWithTimeStamp(sms)

    suspend fun insertEvent(event: Event) = eventsDao.insertEventWithTimeStamp(event)

    suspend fun insertPhoneNumber(phoneNumber: PhoneNumber) =
        phoneNumbersDao.insertSmsWithTimeStamp(phoneNumber)

    suspend fun insertPhoneNumbers(phoneNumbers: List<PhoneNumber>) =
        phoneNumbersDao.insertSinglePhoneNumbers(phoneNumbers)

    suspend fun insertLog(eventLog: EventLog) = eventLogsDao.insertLogWithTimeStamp(eventLog)

    fun getSmsAndPhoneNumbers() = smsDao.getSmsAndPhoneNumbers()

    fun getEventWithSmsAndPhoneNumbers() = eventsDao.getEventWithSmsAndPhoneNumbers()

    fun getUpcomingSMSEvents(timestamp: Long) = eventsDao.getUpcomingSMSEvents(timestamp)

    fun getAllLogs() = eventLogsDao.getAllLogs()

    suspend fun updateEvent(event: Event) = eventsDao.updateEventWithTimeStamp(event)

    fun getEventById(id: Int) = eventsDao.getEventById(id)
}