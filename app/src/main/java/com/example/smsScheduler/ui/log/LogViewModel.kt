package com.example.smsScheduler.ui.log

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.smsScheduler.base.BaseViewModel
import com.example.smsScheduler.data.source.ScheduleRepository
import com.example.smsScheduler.data.source.local.entity.EventLog


class LogViewModel @ViewModelInject constructor(
    scheduleRepository: ScheduleRepository
) :
    BaseViewModel() {

    val logData: LiveData<List<EventLog>> =
        scheduleRepository.getAllLogs().asLiveData()

}