package com.example.scheduledmessenger.ui.add_sms

import android.content.Context
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.scheduledmessenger.R
import com.example.scheduledmessenger.base.BaseViewModel
import com.example.scheduledmessenger.data.source.ScheduleRepository
import com.example.scheduledmessenger.data.source.local.entity.Event
import com.example.scheduledmessenger.data.source.local.entity.EventLog
import com.example.scheduledmessenger.data.source.local.entity.PhoneNumber
import com.example.scheduledmessenger.data.source.local.entity.SMS
import com.example.scheduledmessenger.utils.Constants
import com.example.scheduledmessenger.utils.ManagerAlarm
import com.example.scheduledmessenger.utils.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.*

class AddSmsViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val scheduleRepository: ScheduleRepository,
    private val alarmManager: ManagerAlarm
) :
    BaseViewModel() {

    val etReceiverNumber = ObservableField<String>("")
    private val selectDate = Calendar.getInstance()
    private val receivers: ArrayList<String> = arrayListOf()

    val selectedDateText =
        ObservableField<String>(Utils.dateFormatter.format(selectDate.time).toString())
    val selectedTimeText =
        ObservableField<String>(Utils.timeFormatter.format(selectDate.time).toString())

    val message = ObservableField<String>()
    val messageError = ObservableField<String>()
    val receiverError = ObservableField<String>()

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
        val timePicker = MutableLiveData<Calendar>(null)
        if (isShow)
            timePicker.value = selectDate
        return@switchMap timePicker
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

    fun addSMS() {
        if (validateInput()) {
            viewModelScope.launch {
                showLoader.value = true
                val eventID = scheduleRepository.insertEvent(
                    Event(
                        status = Constants.PENDING,
                        timestamp = selectDate.timeInMillis
                    )
                )

                alarmManager.setAlarm(eventID.toInt(), selectDate.timeInMillis)

                val smsID = scheduleRepository.insertSMS(
                    SMS(
                        eventID = eventID.toInt(),
                        message = message.get().toString()
                    )
                )
                val phoneNumbers = arrayListOf<PhoneNumber>()
                for (number in receivers) {
                    phoneNumbers.add(PhoneNumber(phoneNumber = number, smsID = smsID.toInt()))
                }

                scheduleRepository.insertPhoneNumbers(phoneNumbers)

                scheduleRepository.insertLog(
                    EventLog(
                        logStatus = Constants.SMS_INITIATED,
                        eventID = eventID.toInt()
                    )
                )


                showLoader.value = false
            }
        }

    }

    private fun validateInput(): Boolean {
        var isOkay = true
        hideAllError()
        if (receivers.isEmpty()) {
            receiverError.set(context.getString(R.string.error_empty))
            isOkay = false
        }

        if (message.get().isNullOrEmpty()) {
            messageError.set(context.getString(R.string.error_empty))
            isOkay = false
        }
        return isOkay
    }

    private fun hideAllError() {
        receiverError.set(null)
        messageError.set(null)
    }
}