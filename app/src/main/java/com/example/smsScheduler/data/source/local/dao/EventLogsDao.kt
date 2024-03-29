package com.example.smsScheduler.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.smsScheduler.data.source.local.entity.EventLog
import kotlinx.coroutines.flow.Flow

@Dao
interface EventLogsDao {

    @Insert
    fun insertSingleLog(eventLog: EventLog): Long

    suspend fun insertLogWithTimeStamp(eventLog: EventLog) : Long = insertSingleLog(eventLog.apply {
        timestamp = System.currentTimeMillis()
    })

    @Query("SELECT * FROM EVENTLOG ORDER BY TIMESTAMP DESC")
    fun getAllLogs() : Flow<List<EventLog>>


}