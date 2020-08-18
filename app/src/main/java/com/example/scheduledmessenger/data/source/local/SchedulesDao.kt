package com.example.scheduledmessenger.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.scheduledmessenger.data.LogModel
import com.example.scheduledmessenger.data.SMS
import kotlinx.coroutines.flow.Flow

@Dao
interface SchedulesDao {

    @Query("SELECT COUNT(*) FROM SMS")
    fun getTotalSMSs(): Flow<Int>

    @Query("SELECT COUNT(*) FROM SMS WHERE status=:status")
    fun getSMSsByStatus(status: Int): LiveData<Int>

    @Query("SELECT COUNT(*) FROM SMS WHERE status=2 OR status = 3")
    fun getFailedOrPendingSms(): LiveData<Int>

    /*@get:Query("SELECT COUNT(*) FROM SMSs WHERE status=0")
    val pendingSMSs: LiveData<Int>*/

    @get:Query("SELECT * FROM SMS ORDER BY timestamp DESC")
    val allSMS: LiveData<List<SMS>>

    @get:Query("SELECT * FROM LogModel ORDER BY timestamp DESC")
    val allLogs: LiveData<List<LogModel>>

    @Insert
    fun insertOnlySingleSMS(SMS: SMS): Long

    @Insert
    fun insertSMS(SMSList: List<SMS>)

    @Query("SELECT * FROM SMS WHERE id = :smsID")
    fun getSMSByID(smsID: Int): SMS

    @Query("SELECT COUNT(status) FROM SMS WHERE status = :status")
    fun getQueriedSMSs(status: Int): Int

    @Update
    fun updateSMS(SMS: SMS)

    @Delete
    fun deleteSMS(SMS: SMS)

    // Message log

    @Insert
    fun insertSingleLog(logModel: LogModel): Long
}