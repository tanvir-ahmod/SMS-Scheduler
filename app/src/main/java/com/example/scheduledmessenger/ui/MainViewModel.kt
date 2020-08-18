package com.example.scheduledmessenger.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.scheduledmessenger.base.BaseViewModel
import com.example.scheduledmessenger.data.source.ScheduleRepository

class MainViewModel @ViewModelInject constructor(private val scheduleRepository: ScheduleRepository) :
    BaseViewModel() {

    val totalSms: LiveData<Int> = scheduleRepository.getTotalSMS().asLiveData()
}