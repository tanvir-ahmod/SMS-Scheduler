package com.example.scheduledmessenger.data.source

import com.example.scheduledmessenger.data.source.local.entity.*
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getTotalSMS(): Flow<Int>
    suspend fun insertSMS(sms : SMS) : Long
    suspend fun insertEvent(event: Event) : Long
    suspend fun insertPhoneNumber(phoneNumber: PhoneNumber) : Long
    suspend fun insertPhoneNumbers(phoneNumbers: List<PhoneNumber>)
    suspend fun insertLog(eventLog: EventLog) : Long
    fun getSmsAndPhoneNumbers(): Flow<List<SmsAndPhoneNumbers>>
    fun getEventWithSmsAndPhoneNumbers(): Flow<List<EventWithSmsAndPhoneNumbers>>
}