package com.example.scheduledmessenger.ui.add_sms

import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.scheduledmessenger.base.BaseViewModel

class AddSmsViewModel @ViewModelInject constructor() :
    BaseViewModel() {

    private val receivers: ArrayList<String> = arrayListOf()

    private val _receiverNumbers = MutableLiveData<List<String>>()
    val receiverNumbers: LiveData<List<String>> = _receiverNumbers

    val etReceiverNumber = ObservableField<String>("")

    fun addReceiverNumber() {
        receivers.add(etReceiverNumber.get().toString())
        _receiverNumbers.value = receivers
        etReceiverNumber.set("")
    }

    fun removeNumber(position: Int) {
        receivers.removeAt(position)
        _receiverNumbers.value = receivers
    }

}