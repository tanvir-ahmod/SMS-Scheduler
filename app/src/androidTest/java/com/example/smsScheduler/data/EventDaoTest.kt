package com.example.smsScheduler.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.example.smsScheduler.data.source.local.ScheduleMessengerDatabase
import com.example.smsScheduler.data.source.local.dao.EventsDao
import com.example.smsScheduler.data.source.local.entity.Event
import com.example.smsScheduler.utils.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class EventDaoTest {
    private lateinit var database: ScheduleMessengerDatabase
    private lateinit var eventsDao: EventsDao

    @get:Rule
    val instantTaskExecutorRule  = InstantTaskExecutorRule()

    @Before
    fun createDB() {
        runBlocking {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            database =
                Room.inMemoryDatabaseBuilder(context, ScheduleMessengerDatabase::class.java).build()
            eventsDao = database.eventsDao()
        }
    }

    @After
    fun closeDB(){
        database.close()
    }

    @Test
    fun testGetAllEvents() = runBlocking {
        eventsDao.insertEvent(Event(status = Constants.PENDING, timestamp = System.currentTimeMillis()))
        assertThat(eventsDao.getEventWithSmsAndPhoneNumbersOrderByAsc().first().size, equalTo(1))
    }

    @Test
    fun testGetEventById() = runBlocking {
        eventsDao.insertEvent(Event(id = 500, status = Constants.PENDING, timestamp = System.currentTimeMillis()))
        assertThat(eventsDao.getEventById(500).id, equalTo(500))
    }

    @Test
    fun testGetUpcomingEvents() = runBlocking {
        val datePlusOneMonth = Calendar.getInstance().run {
            add(Calendar.MONTH, 1)
            time
        }
        eventsDao.insertEvent(Event( status = Constants.PENDING, timestamp = datePlusOneMonth.time))
        assertThat(eventsDao.getUpcomingEvents(System.currentTimeMillis()).size, equalTo(1))
    }
}