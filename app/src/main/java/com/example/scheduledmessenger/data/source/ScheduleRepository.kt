package com.example.scheduledmessenger.data.source

import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getTotalSMS(): Flow<Int>
}