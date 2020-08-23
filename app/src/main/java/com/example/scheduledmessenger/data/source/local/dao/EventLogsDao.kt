package com.example.scheduledmessenger.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.scheduledmessenger.data.source.local.db_models.EventLog

@Dao
interface EventLogsDao {

    @Insert
    fun insertSingleLog(eventLog: EventLog): Long

    suspend fun insertLogWithTimeStamp(eventLog: EventLog) : Long = insertSingleLog(eventLog.apply {
        createdAt = System.currentTimeMillis()
        updatedAt = System.currentTimeMillis()
    })


}