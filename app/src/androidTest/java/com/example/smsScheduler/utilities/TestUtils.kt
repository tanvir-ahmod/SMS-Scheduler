package com.example.smsScheduler.utilities

import android.util.EventLog
import com.example.smsScheduler.data.source.local.entity.Event
import com.example.smsScheduler.utils.Constants

val events = arrayListOf(
    Event(status = Constants.PENDING, timestamp = System.currentTimeMillis()),
    Event(status = Constants.SENT, timestamp = System.currentTimeMillis()),
    Event(status = Constants.FAILED, timestamp = System.currentTimeMillis()),
)