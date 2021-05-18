package com.example.smsScheduler.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.example.smsScheduler.data.source.local.ScheduleMessengerDatabase
import com.example.smsScheduler.data.source.local.dao.EventLogsDao
import com.example.smsScheduler.data.source.local.dao.EventsDao
import com.example.smsScheduler.data.source.local.entity.Event
import com.example.smsScheduler.data.source.local.entity.EventLog
import com.example.smsScheduler.utils.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventLogsDaoTest {
    private lateinit var database: ScheduleMessengerDatabase
    private lateinit var eventLogsDao: EventLogsDao
    private lateinit var eventsDao: EventsDao

    @get:Rule
    val instantTaskExecutorRule  = InstantTaskExecutorRule()

    @Before
    fun createDB() {
        runBlocking {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            database =
                Room.inMemoryDatabaseBuilder(context, ScheduleMessengerDatabase::class.java).build()
            eventLogsDao = database.eventLogsDao()
            eventsDao = database.eventsDao()
            eventsDao.insertEvent(
                Event(
                    id = 1,
                    status = Constants.PENDING,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    @After
    fun closeDB(){
        database.close()
    }

    @Test
    fun testGetAllLogs() = runBlocking{
        eventLogsDao.insertSingleLog(EventLog(eventID = 1, logStatus = 1))
        assertThat(eventLogsDao.getAllLogs().first().size, equalTo(1))
    }
}