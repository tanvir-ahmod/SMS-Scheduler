package com.example.smsScheduler.ui.timeline

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smsScheduler.base.BaseViewModel
import com.example.smsScheduler.data.contacts.ContactsRepository
import com.example.smsScheduler.data.source.ScheduleRepository
import com.example.smsScheduler.ui.adapter.EventModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class TimelineViewModel @ViewModelInject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val contactsRepository: ContactsRepository
) :
    BaseViewModel() {

    private val _timeLineData = MutableLiveData<List<EventModel>>()
    val timeLineData: LiveData<List<EventModel>> = _timeLineData

    var isSortDescending = false

    init {
        getTimelineData()
    }

    private fun getTimelineData() {
        viewModelScope.launch {

            val allEvents = scheduleRepository.getEventWithSmsAndPhoneNumbers(isSortDescending)
            allEvents.collect { events ->
                val timelineData = mutableListOf<EventModel>()
                for (event in events) {
                    event.smsAndPhoneNumbers?.let { phone ->
                        val names =
                            contactsRepository.getContactNamesByPhoneNumbers(phone.phoneNumbers)

                        timelineData.add(
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
                _timeLineData.value = timelineData
            }
        }

    }

    fun updateSortOrder() {
        isSortDescending = !isSortDescending
        getTimelineData()
    }

}