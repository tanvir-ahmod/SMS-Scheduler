package com.example.scheduledmessenger.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduledmessenger.base.BaseViewModel
import com.example.scheduledmessenger.data.source.ScheduleRepository
import com.example.scheduledmessenger.data.source.local.entity.EventLog
import com.example.scheduledmessenger.data.source.local.entity.EventWithSmsAndPhoneNumbers
import com.example.scheduledmessenger.utils.Constants
import kotlinx.coroutines.launch

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

    init {
        updateFailStatus()
    }

    private fun updateFailStatus() {
        viewModelScope.launch {
            val events = scheduleRepository.getFailedEvents(System.currentTimeMillis())

            for (event in events) {
                event.status = Constants.FAILED
                scheduleRepository.updateEvent(event)

                scheduleRepository.insertLog(
                    EventLog(
                        logStatus = Constants.SMS_FAILED,
                        eventID = event.id
                    )
                )
            }
        }
    }

}