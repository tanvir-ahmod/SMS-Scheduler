package com.example.scheduledmessenger.data.source

import com.example.scheduledmessenger.data.source.local.entity.*
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getTotalSMS(): Flow<Int>
    fun getSMSsWithStatus(status : Int): Flow<Int>
    suspend fun insertSMS(sms : SMS) : Long
    suspend fun insertEvent(event: Event) : Long
    suspend fun insertPhoneNumber(phoneNumber: PhoneNumber) : Long
    suspend fun insertPhoneNumbers(phoneNumbers: List<PhoneNumber>)
    suspend fun insertLog(eventLog: EventLog) : Long
    fun getSmsAndPhoneNumbers(): Flow<List<SmsAndPhoneNumbers>>
    fun getEventWithSmsAndPhoneNumbers(): Flow<List<EventWithSmsAndPhoneNumbers>>
    fun getUpcomingSMSEvents(timestamp : Long) : Flow<List<EventWithSmsAndPhoneNumbers>>
    fun getAllLogs():  Flow<List<EventLog>>
    suspend fun updateEvent(event: Event) : Int
    suspend fun getEventById(id: Int) : Event
    fun getUpcomingEvents(timestamp: Long) : List<Event>
    suspend fun getSmsAndPhoneNumbersWithEventId(id: Int) :  SmsAndPhoneNumbers
    suspend fun updateSms(sms: SMS) : Int
    suspend fun deletePhoneNumber(phoneNumber: PhoneNumber)
}