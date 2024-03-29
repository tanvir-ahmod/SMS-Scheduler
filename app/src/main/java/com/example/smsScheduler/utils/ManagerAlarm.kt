package com.example.smsScheduler.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ManagerAlarm @Inject constructor(@ApplicationContext private val context: Context) {

    private var alarmMgr: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private lateinit var alarmIntent: PendingIntent

    fun setAlarm(id: Int, timestamp: Long) {

        alarmIntent = Intent(context, AlarmBroadCastReceiver::class.java).let { intent ->
            intent.putExtra(Constants.ID, id)
            PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timestamp,
            alarmIntent
        )
    }

    fun updateAlarm(id: Int, timestamp: Long){
        dismissAlarm(id)
        setAlarm(id, timestamp)
    }
    fun dismissAlarm(id: Int) {
        alarmIntent = Intent(context, AlarmBroadCastReceiver::class.java).let { intent ->
            intent.putExtra(Constants.ID, id)
            PendingIntent.getBroadcast(context, id, intent, 0)
        }
        alarmMgr.cancel(alarmIntent)
    }
}