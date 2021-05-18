package com.example.smsScheduler.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.example.smsScheduler.data.source.local.ScheduleMessengerDatabase
import com.example.smsScheduler.data.source.local.dao.EventsDao
import com.example.smsScheduler.data.source.local.dao.SmsDao
import com.example.smsScheduler.data.source.local.entity.Event
import com.example.smsScheduler.data.source.local.entity.SMS
import com.example.smsScheduler.utils.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class SmsDaoTest {
    private lateinit var database: ScheduleMessengerDatabase
    private lateinit var smsDao: SmsDao
    private lateinit var eventsDao: EventsDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDB() {
        runBlocking {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            database =
                Room.inMemoryDatabaseBuilder(context, ScheduleMessengerDatabase::class.java).build()
            smsDao = database.smsDao()
            eventsDao = database.eventsDao()
            val datePlusOneMonth = Calendar.getInstance().run {
                add(Calendar.MONTH, 1)
                time
            }
            eventsDao.insertEvent(
                Event(
                    id = 1,
                    status = Constants.PENDING,
                    timestamp = datePlusOneMonth.time
                )
            )
            eventsDao.insertEvent(
                Event(
                    id = 2,
                    status = Constants.PENDING,
                    timestamp = datePlusOneMonth.time
                )
            )
        }
    }

    @After
    fun closeDB() {
        database.close()
    }

    @Test
    fun testGetTotalSMSs() = runBlocking {
        smsDao.insertSingleSMS(SMS(eventID = 1, message = "test", subscriptionID = 1))
        ViewMatchers.assertThat(
            smsDao.getTotalSMSs().first(),
            CoreMatchers.equalTo(1)
        )
    }

    @Test
    fun testUpdatingSms() = runBlocking {
        val sms = SMS(id = 1, eventID = 1, message = "test", subscriptionID = 1)
        smsDao.insertSingleSMS(sms)
        sms.message = "updated"
        smsDao.updateSMS(sms)
        ViewMatchers.assertThat(
            smsDao.getSmsAndPhoneNumbersWithEventId(1).sms.message,
            CoreMatchers.equalTo("updated")
        )
    }

    @Test
    fun testGetSmsAndPhoneNumbers() = runBlocking {
        val sms = SMS(id = 1, eventID = 1, message = "test", subscriptionID = 1)
        val sms2 = SMS(id = 2, eventID = 2, message = "test", subscriptionID = 1)
        smsDao.insertSingleSMS(sms)
        smsDao.insertSingleSMS(sms2)
        ViewMatchers.assertThat(
            smsDao.getSmsAndPhoneNumbers().first().size,
            CoreMatchers.equalTo(2)
        )
    }
}