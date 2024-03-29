package com.example.smsScheduler.data.source.local

import com.example.smsScheduler.data.source.local.dao.EventsDao
import com.example.smsScheduler.data.source.local.dao.EventLogsDao
import com.example.smsScheduler.data.source.local.dao.PhoneNumbersDao
import com.example.smsScheduler.data.source.local.dao.SmsDao
import com.example.smsScheduler.data.source.local.entity.*
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

    fun getEventWithSmsAndPhoneNumbers(isSortDescending: Boolean) =
        if (isSortDescending) eventsDao.getEventWithSmsAndPhoneNumbersOrderByDesc()
        else eventsDao.getEventWithSmsAndPhoneNumbersOrderByAsc()


    fun getUpcomingSMSEvents(timestamp: Long) = eventsDao.getUpcomingSMSEvents(timestamp)

    fun getAllLogs() = eventLogsDao.getAllLogs()

    suspend fun updateEvent(event: Event) = eventsDao.updateEventWithTimeStamp(event)

    suspend fun getEventById(id: Int) = eventsDao.getEventById(id)

    fun getUpcomingEvents(timestamp: Long) = eventsDao.getUpcomingEvents(timestamp)

    suspend fun getSmsAndPhoneNumbersWithEventId(id: Int) =
        smsDao.getSmsAndPhoneNumbersWithEventId(id)

    suspend fun updateSms(sms: SMS) = smsDao.updateSMS(sms)

    suspend fun deletePhoneNumber(phoneNumber: PhoneNumber) =
        phoneNumbersDao.deletePhoneNumber(phoneNumber)

    suspend fun deleteEvent(event: Event) =
        eventsDao.deleteEvent(event)

    suspend fun deleteEventById(id: Int) =
        eventsDao.deleteEventById(id)

    suspend fun getFailedEvents(timestamp: Long) = eventsDao.getFailedEvents(timestamp)

}