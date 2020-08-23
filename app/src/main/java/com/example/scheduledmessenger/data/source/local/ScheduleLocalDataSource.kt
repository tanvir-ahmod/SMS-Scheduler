package com.example.scheduledmessenger.data.source.local

import com.example.scheduledmessenger.data.source.local.dao.EventsDao
import com.example.scheduledmessenger.data.source.local.dao.SmsDao
import com.example.scheduledmessenger.data.source.local.db_models.Event
import com.example.scheduledmessenger.data.source.local.db_models.SMS
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleLocalDataSource @Inject constructor(private val eventsDao: EventsDao,
                                                  private val smsDao: SmsDao) {
    fun getTotalSms(): Flow<Int> = smsDao.getTotalSMSs()

    fun insertSMS(sms: SMS) = smsDao.insertSingleSMS(sms)

    suspend fun insertEvent(event: Event) = eventsDao.insertEventWithTimeStamp(event)
}