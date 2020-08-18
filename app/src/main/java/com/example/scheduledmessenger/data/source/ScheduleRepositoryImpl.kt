package com.example.scheduledmessenger.data.source

import com.example.scheduledmessenger.data.source.local.ScheduleLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val eventsDataSource: ScheduleLocalDataSource
) :
    ScheduleRepository {
    override fun getTotalSMS(): Flow<Int> = eventsDataSource.getTotalSms()
}