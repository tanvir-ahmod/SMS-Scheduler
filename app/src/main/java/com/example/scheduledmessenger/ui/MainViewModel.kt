package com.example.scheduledmessenger.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduledmessenger.base.BaseViewModel
import com.example.scheduledmessenger.data.source.ScheduleRepository
import com.example.scheduledmessenger.data.source.local.entity.Event
import com.example.scheduledmessenger.utils.ManagerAlarm
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val alarmManager: ManagerAlarm
) :
    BaseViewModel() {

    val contactNumber = MutableLiveData<String>()

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            alarmManager.dismissAlarm(event.id)
            scheduleRepository.deleteEvent(event)
        }
    }
}