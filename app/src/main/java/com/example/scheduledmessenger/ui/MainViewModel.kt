package com.example.scheduledmessenger.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.example.scheduledmessenger.base.BaseViewModel

class MainViewModel @ViewModelInject constructor() :
    BaseViewModel() {

    val contactNumber = MutableLiveData<String>()
}