package com.example.scheduledmessenger.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.scheduledmessenger.base.BaseBroadCastReceiver
import com.example.scheduledmessenger.data.source.ScheduleRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmBroadCastReceiver : BaseBroadCastReceiver() {

    @Inject
    lateinit var smsSender: SMSSender

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val id = intent.getIntExtra(Constants.ID, 0)
        smsSender.sendSms(id)
    }
}