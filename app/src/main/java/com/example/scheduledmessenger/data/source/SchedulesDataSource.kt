package com.example.scheduledmessenger.data.source

import kotlinx.coroutines.flow.Flow

interface SchedulesDataSource {
    fun getTotalSms(): Flow<Int>
}
