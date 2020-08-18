package com.example.scheduledmessenger.data.source.local

import com.example.scheduledmessenger.data.source.SchedulesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleLocalDataSource @Inject constructor(private val schedulesDao: SchedulesDao) :
    SchedulesDataSource {
    override fun getTotalSms(): Flow<Int> = schedulesDao.getTotalSMSs()
}