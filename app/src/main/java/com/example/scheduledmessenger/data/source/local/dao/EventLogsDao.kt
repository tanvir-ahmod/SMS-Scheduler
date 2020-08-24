package com.example.scheduledmessenger.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.scheduledmessenger.data.source.local.entity.EventLog
import kotlinx.coroutines.flow.Flow

@Dao
interface EventLogsDao {

    @Insert
    fun insertSingleLog(eventLog: EventLog): Long

    suspend fun insertLogWithTimeStamp(eventLog: EventLog) : Long = insertSingleLog(eventLog.apply {
        timestamp = System.currentTimeMillis()
    })

    @Query("SELECT * FROM EVENTLOG")
    fun getAllLogs() : Flow<List<EventLog>>


}