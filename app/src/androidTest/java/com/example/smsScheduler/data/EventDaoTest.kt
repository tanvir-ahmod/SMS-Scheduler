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
        eventsDao.insertEvent(Event( status = Constants.SENT, timestamp = System.currentTimeMillis()))
        assertThat(eventsDao.getUpcomingEvents(System.currentTimeMillis()).size, equalTo(1))
    }

    @Test
    fun testUpdatingEvents() = runBlocking {
        val event = Event( id = 1, status = Constants.PENDING, timestamp = System.currentTimeMillis())
        eventsDao.insertEvent(event)
        event.status = Constants.SENT
        eventsDao.updateEvent(event)
        assertThat(eventsDao.getEventById(1).status, equalTo(Constants.SENT))
    }

    @Test
    fun testDeletingEvents() = runBlocking {
        val event = Event( id = 1, status = Constants.PENDING, timestamp = System.currentTimeMillis())
        eventsDao.insertEvent(event)
        eventsDao.deleteEvent(event)
        assertThat(eventsDao.getEventWithSmsAndPhoneNumbersOrderByAsc().first().size, equalTo(0))
    }

    @Test
    fun testDeletingEventsByID() = runBlocking {
        val event = Event( id = 1, status = Constants.PENDING, timestamp = System.currentTimeMillis())
        eventsDao.insertEvent(event)
        eventsDao.deleteEventById(1)
        assertThat(eventsDao.getEventWithSmsAndPhoneNumbersOrderByAsc().first().size, equalTo(0))
    }

    @Test
    fun testEventWithSmsAndPhoneNumbersOrderByDesc() = runBlocking {
        val event1 = Event( id = 1, status = Constants.PENDING, timestamp = System.currentTimeMillis())
        val event2 = Event( id = 2, status = Constants.PENDING, timestamp = System.currentTimeMillis())
        val event3 = Event( id = 3, status = Constants.PENDING, timestamp = System.currentTimeMillis())
        eventsDao.insertEvent(event1)
        eventsDao.insertEvent(event2)
        eventsDao.insertEvent(event3)
        val events = eventsDao.getEventWithSmsAndPhoneNumbersOrderByAsc().first()

        var isSortedDec = true
        for( i in 0..2){
            if(events[i].event.timestamp > event3.timestamp){
                isSortedDec = false
                break
            }
        }

        assertThat(isSortedDec, equalTo(true))
    }

    @Test
    fun testEventWithSmsAndPhoneNumbersOrderByAsc() = runBlocking {
        val event1 = Event( id = 1, status = Constants.PENDING, timestamp = System.currentTimeMillis())
        val event2 = Event( id = 2, status = Constants.PENDING, timestamp = System.currentTimeMillis())
        val event3 = Event( id = 3, status = Constants.PENDING, timestamp = System.currentTimeMillis())
        eventsDao.insertEvent(event1)
        eventsDao.insertEvent(event2)
        eventsDao.insertEvent(event3)
        val events = eventsDao.getEventWithSmsAndPhoneNumbersOrderByAsc().first()

        var isSortedAsc = true
        for( i in 2 downTo 1){
            if(events[i].event.timestamp < event1.timestamp){
                isSortedAsc = false
                break
            }
        }

        assertThat(isSortedAsc, equalTo(true))
    }

    @Test
    fun testGetFailedEvents() = runBlocking {
        val dateMinusOneMonth = Calendar.getInstance().run {
            add(Calendar.MONTH, -1)
            time
        }
        val event = Event( id = 1, status = Constants.PENDING, timestamp = dateMinusOneMonth.time)
        eventsDao.insertEvent(event)
        assertThat(eventsDao.getFailedEvents(System.currentTimeMillis()).size, equalTo(1))
    }
}