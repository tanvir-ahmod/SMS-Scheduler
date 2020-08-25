package com.example.scheduledmessenger.utils

import android.content.Context
import android.content.Intent
import com.example.scheduledmessenger.base.BaseReceiver
import com.example.scheduledmessenger.data.source.ScheduleRepository
import com.example.scheduledmessenger.data.source.local.entity.EventLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BaseReceiver() {

    @Inject
    lateinit var scheduleRepository: ScheduleRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val id = intent.getIntExtra(Constants.ID, 0)
        GlobalScope.launch(Dispatchers.IO) {
            val event = scheduleRepository.getEventById(id)
            event.status = Constants.SENT

            scheduleRepository.updateEvent(event)
            scheduleRepository.insertLog(
                EventLog(
                    logStatus = Constants.SMS_SENT,
                    eventID = id
                )
            )

        }

    }
}