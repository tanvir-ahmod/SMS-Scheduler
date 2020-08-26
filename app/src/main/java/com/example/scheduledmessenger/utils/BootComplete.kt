package com.example.scheduledmessenger.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.scheduledmessenger.base.BaseBroadCastReceiver
import com.example.scheduledmessenger.data.source.ScheduleRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootComplete : BaseBroadCastReceiver() {

    @Inject
    lateinit var scheduleRepository: ScheduleRepository

    @Inject
    lateinit var alarmManager: ManagerAlarm

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            GlobalScope.launch {
                val eventsToSetAlarm =
                    scheduleRepository.getUpcomingEvents(System.currentTimeMillis())

                for (event in eventsToSetAlarm) {
                    alarmManager.setAlarm(event.id, event.timestamp)
                }
            }
        }
    }
}