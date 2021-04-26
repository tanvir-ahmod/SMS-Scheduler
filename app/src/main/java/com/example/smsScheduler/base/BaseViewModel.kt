package com.example.smsScheduler.base
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    val showLoader = MutableLiveData<Boolean>(false)
    val showMessage = MutableLiveData<String>()
}