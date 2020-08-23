package com.example.scheduledmessenger.data.source.local

import com.example.scheduledmessenger.data.source.local.dao.EventsDao
import com.example.scheduledmessenger.data.source.local.dao.EventLogsDao
import com.example.scheduledmessenger.data.source.local.dao.PhoneNumbersDao
import com.example.scheduledmessenger.data.source.local.dao.SmsDao
import com.example.scheduledmessenger.data.source.local.db_models.Event
import com.example.scheduledmessenger.data.source.local.db_models.EventLog
import com.example.scheduledmessenger.data.source.local.db_models.PhoneNumber
import com.example.scheduledmessenger.data.source.local.db_models.SMS
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleLocalDataSource @Inject constructor(
    private val eventsDao: EventsDao,
    private val smsDao: SmsDao,
    private val phoneNumbersDao: PhoneNumbersDao,
    private val eventLogsDao: EventLogsDao
) {
    fun getTotalSms(): Flow<Int> = smsDao.getTotalSMSs()

    suspend fun insertSMS(sms: SMS): Long = smsDao.insertSmsWithTimeStamp(sms)

    suspend fun insertEvent(event: Event) = eventsDao.insertEventWithTimeStamp(event)

    suspend fun insertPhoneNumber(phoneNumber: PhoneNumber) = phoneNumbersDao.insertSmsWithTimeStamp(phoneNumber)

    suspend fun insertPhoneNumbers(phoneNumbers: List<PhoneNumber>) = phoneNumbersDao.insertSinglePhoneNumbers(phoneNumbers)

    suspend fun insertLog(eventLog: EventLog) = eventLogsDao.insertLogWithTimeStamp(eventLog)
}