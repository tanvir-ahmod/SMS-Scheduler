package com.example.smsScheduler.utils

import android.content.Context
import android.content.Intent
import com.example.smsScheduler.base.BaseBroadCastReceiver
import com.example.smsScheduler.data.source.ScheduleRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootCompleteBroadCastReceiver : BaseBroadCastReceiver() {

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