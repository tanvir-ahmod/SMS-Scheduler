package com.example.scheduledmessenger.ui.add_sms

import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.scheduledmessenger.base.BaseViewModel
import com.example.scheduledmessenger.utils.Utils
import java.util.*
import kotlin.collections.ArrayList

class AddSmsViewModel @ViewModelInject constructor() :
    BaseViewModel() {

    val etReceiverNumber = ObservableField<String>("")
    private val selectDate = Calendar.getInstance()
    private val receivers: ArrayList<String> = arrayListOf()

    var selectedDateText =
        ObservableField<String>(Utils.dateFormatter.format(selectDate.time).toString())
    var selectedTimeText =
        ObservableField<String>(Utils.timeFormatter.format(selectDate.time).toString())

    private val _receiverNumbers = MutableLiveData<List<String>>()
    val receiverNumbers: LiveData<List<String>> = _receiverNumbers

    private val _showDatePicker = MutableLiveData<Boolean>(false)
    val showDatePicker: LiveData<Calendar> = _showDatePicker.switchMap { isShow ->
        val datePicker = MutableLiveData<Calendar>(null)
        if (isShow)
            datePicker.value = selectDate
        return@switchMap datePicker
    }

    private val _showTimePicker = MutableLiveData<Boolean>(false)
    val showTimePicker: LiveData<Calendar> = _showTimePicker.switchMap { isShow ->
        val datePicker = MutableLiveData<Calendar>(null)
        if (isShow)
            datePicker.value = selectDate
        return@switchMap datePicker
    }

    fun addReceiverNumber() {
        receivers.add(etReceiverNumber.get().toString())
        _receiverNumbers.value = receivers
        etReceiverNumber.set("")
    }

    fun removeNumber(position: Int) {
        receivers.removeAt(position)
        _receiverNumbers.value = receivers
    }

    fun showDatePicker() {
        _showDatePicker.value = true
    }

    fun showTimePicker() {
        _showTimePicker.value = true
    }

    fun changeDate(year: Int, month: Int, date: Int) {
        selectDate.set(Calendar.YEAR, year)
        selectDate.set(Calendar.MONTH, month)
        selectDate.set(Calendar.DAY_OF_MONTH, date)
        selectedDateText.set(Utils.dateFormatter.format(selectDate.time))
        _showDatePicker.value = false
    }

    fun changeTime(hour: Int, minute: Int) {
        selectDate.set(Calendar.HOUR_OF_DAY, hour)
        selectDate.set(Calendar.MINUTE, minute)
        selectedTimeText.set(Utils.timeFormatter.format(selectDate.time))
        _showTimePicker.value = false
    }

}