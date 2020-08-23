package com.example.scheduledmessenger.data.source

import com.example.scheduledmessenger.data.source.local.db_models.Event
import com.example.scheduledmessenger.data.source.local.db_models.SMS
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getTotalSMS(): Flow<Int>
    suspend fun addSMS(sms : SMS) : Long
    suspend fun insertEvent(event: Event) : Long
}