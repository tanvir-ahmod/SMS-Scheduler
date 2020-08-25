package com.example.scheduledmessenger.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.scheduledmessenger.base.BaseViewModel
import com.example.scheduledmessenger.data.source.ScheduleRepository
import com.example.scheduledmessenger.data.source.local.entity.EventWithSmsAndPhoneNumbers
import com.example.scheduledmessenger.utils.Constants

class HomeViewModel @ViewModelInject constructor(private val scheduleRepository: ScheduleRepository) :
    BaseViewModel() {

    val totalSms: LiveData<Int> = scheduleRepository.getTotalSMS().asLiveData()
    val totalSentSms: LiveData<Int> =
        scheduleRepository.getSMSsWithStatus(Constants.SENT).asLiveData()
    val totalPendingSms: LiveData<Int> =
        scheduleRepository.getSMSsWithStatus(Constants.PENDING).asLiveData()
    val totalFailedSms: LiveData<Int> =
        scheduleRepository.getSMSsWithStatus(Constants.FAILED).asLiveData()

    val upcomingEvents: LiveData<List<EventWithSmsAndPhoneNumbers>> =
        scheduleRepository.getUpcomingSMSEvents(System.currentTimeMillis()).asLiveData()

}