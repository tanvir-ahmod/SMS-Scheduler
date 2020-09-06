package com.example.scheduledmessenger.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduledmessenger.base.BaseViewModel
import com.example.scheduledmessenger.data.contacts.ContactsRepository
import com.example.scheduledmessenger.data.source.ScheduleRepository
import com.example.scheduledmessenger.data.source.local.entity.EventLog
import com.example.scheduledmessenger.ui.adapter.EventModel
import com.example.scheduledmessenger.utils.Constants
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val contactsRepository: ContactsRepository
) :
    BaseViewModel() {
    val totalSms: LiveData<Int> = scheduleRepository.getTotalSMS().asLiveData()
    val totalSentSms: LiveData<Int> =
        scheduleRepository.getSMSsWithStatus(Constants.SENT).asLiveData()
    val totalPendingSms: LiveData<Int> =
        scheduleRepository.getSMSsWithStatus(Constants.PENDING).asLiveData()
    val totalFailedSms: LiveData<Int> =
        scheduleRepository.getSMSsWithStatus(Constants.FAILED).asLiveData()

    private val _upcomingEvents = MutableLiveData<List<EventModel>>()
    val upcomingEvents: LiveData<List<EventModel>> = _upcomingEvents

    init {
        updateFailStatus()
        getUpcomingEvents()
    }

    private fun getUpcomingEvents() {
        viewModelScope.launch {
            val upcomingEvents = scheduleRepository.getUpcomingSMSEvents(System.currentTimeMillis())
            upcomingEvents.collect { events ->
                val upcomingEventData = mutableListOf<EventModel>()
                for (event in events) {
                    event.smsAndPhoneNumbers?.let {phone->
                        val names =
                            contactsRepository.getContactNamesByPhoneNumbers(phone.phoneNumbers)

                        upcomingEventData.add(
                            EventModel(
                                event.event.id,
                                names,
                                event.smsAndPhoneNumbers.sms.message,
                                event.event.timestamp,
                                event.event.status
                            )
                        )
                    }

                }
                _upcomingEvents.value = upcomingEventData
            }
        }
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