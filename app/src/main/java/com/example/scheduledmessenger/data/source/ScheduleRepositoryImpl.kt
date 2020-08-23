package com.example.scheduledmessenger.data.source

import com.example.scheduledmessenger.data.source.local.db_models.SMS
import com.example.scheduledmessenger.data.source.local.ScheduleLocalDataSource
import com.example.scheduledmessenger.data.source.local.db_models.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val eventsDataSource: ScheduleLocalDataSource
) :
    ScheduleRepository {
    override fun getTotalSMS(): Flow<Int> = eventsDataSource.getTotalSms()
    override suspend fun addSMS(sms: SMS): Long =
        withContext(Dispatchers.IO) { eventsDataSource.insertSMS(sms) }

    override suspend fun insertEvent(event: Event): Long =
        withContext(Dispatchers.IO) { eventsDataSource.insertEvent(event) }
}