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

    val showAddContactBtn = ObservableField<Boolean>(false)
    val etReceiverNumber = ObservableField<String>()

    fun setReceiverNumber(number: String) {
        showAddContactBtn.set(number.isNotEmpty())
    }

    fun addReceiverNumber() {
        receivers.add(etReceiverNumber.get().toString())
        _receiverNumbers.value = receivers
        etReceiverNumber.set("")
        showAddContactBtn.set(false)
    }

    fun removeNumber(position: Int) {
        receivers.removeAt(position)
        _receiverNumbers.value = receivers
    }

}