package com.example.scheduledmessenger.ui.timeline

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduledmessenger.base.BaseViewModel
import com.example.scheduledmessenger.data.source.ScheduleRepository
import com.example.scheduledmessenger.data.source.local.entity.Event
import com.example.scheduledmessenger.data.source.local.entity.EventWithSmsAndPhoneNumbers
import kotlinx.coroutines.launch


class TimelineViewModel @ViewModelInject constructor(
    private val scheduleRepository: ScheduleRepository
) :
    BaseViewModel() {

    val timeLineData: LiveData<List<EventWithSmsAndPhoneNumbers>> =
        scheduleRepository.getEventWithSmsAndPhoneNumbers().asLiveData()



}