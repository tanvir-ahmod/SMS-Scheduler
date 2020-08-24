package com.example.scheduledmessenger.ui.log

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.scheduledmessenger.base.BaseViewModel
import com.example.scheduledmessenger.data.source.ScheduleRepository
import com.example.scheduledmessenger.data.source.local.entity.EventAndLogs
import com.example.scheduledmessenger.data.source.local.entity.EventLog


class LogViewModel @ViewModelInject constructor(
    private val scheduleRepository: ScheduleRepository
) :
    BaseViewModel() {

    val logData: LiveData<List<EventLog>> =
        scheduleRepository.getAllLogs().asLiveData()

}