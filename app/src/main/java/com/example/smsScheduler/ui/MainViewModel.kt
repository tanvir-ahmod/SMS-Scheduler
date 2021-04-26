package com.example.smsScheduler.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.example.smsScheduler.base.BaseViewModel

class MainViewModel @ViewModelInject constructor() :
    BaseViewModel() {

    val contactNumber = MutableLiveData<String>()
}