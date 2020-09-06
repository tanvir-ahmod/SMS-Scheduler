package com.example.scheduledmessenger.ui.add_sms

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.widget.CheckBox
import androidx.core.app.ActivityCompat
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
import com.example.scheduledmessenger.utils.TriggeredEvent
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

    private var eventId = 0

    val etReceiverNumber = ObservableField<String>("")
    val showCancelButton = ObservableField<Boolean>(false)
    private val selectDate = Calendar.getInstance()
    private val receivers: ArrayList<String> = arrayListOf()
    val selectedDateText =
        ObservableField<String>(Utils.dateFormatter.format(selectDate.time).toString())
    val selectedTimeText =
        ObservableField<String>(Utils.timeFormatter.format(selectDate.time).toString())

    val isFormEditable = ObservableField<Boolean>(true)
    val etMessage = ObservableField<String>()
    val messageError = ObservableField<String>()
    val receiverError = ObservableField<String>()

    private val _popBack = MutableLiveData<TriggeredEvent<Boolean>>()
    val popBack: LiveData<TriggeredEvent<Boolean>> = _popBack

    private val _actionBarText = MutableLiveData<String>("Add SMS")
    val actionBarText: LiveData<String> = _actionBarText

    private val _receiverNumbers = MutableLiveData<List<String>>()
    val receiverNumbers: LiveData<List<String>> = _receiverNumbers

    private val _showDatePicker = MutableLiveData<TriggeredEvent<Calendar>>()
    val showDatePicker: LiveData<TriggeredEvent<Calendar>> = _showDatePicker

    private val _showTimePicker = MutableLiveData<TriggeredEvent<Calendar>>()
    val showTimePicker: LiveData<TriggeredEvent<Calendar>> = _showTimePicker

    private val _availableSims = MutableLiveData<List<CheckBox>>()
    val availableSims: LiveData<List<CheckBox>> = _availableSims
    private val sims: ArrayList<CheckBox> = arrayListOf()

    fun addReceiverNumber() {
        if (!isFormEditable.get()!!)
            return

        hideAllError()
        receivers.add(etReceiverNumber.get().toString())
        _receiverNumbers.value = receivers
        etReceiverNumber.set("")
    }

    fun removeNumber(position: Int) {
        if (!isFormEditable.get()!!)
            return

        receivers.removeAt(position)
        _receiverNumbers.value = receivers
    }

    fun showDatePicker() {
        _showDatePicker.value = TriggeredEvent(selectDate)
    }

    fun showTimePicker() {
        _showTimePicker.value = TriggeredEvent(selectDate)
    }

    fun changeDate(year: Int, month: Int, date: Int) {
        selectDate.set(Calendar.YEAR, year)
        selectDate.set(Calendar.MONTH, month)
        selectDate.set(Calendar.DAY_OF_MONTH, date)
        selectedDateText.set(Utils.dateFormatter.format(selectDate.time))
    }

    fun changeTime(hour: Int, minute: Int) {
        selectDate.set(Calendar.HOUR_OF_DAY, hour)
        selectDate.set(Calendar.MINUTE, minute)
        selectedTimeText.set(Utils.timeFormatter.format(selectDate.time))
    }

    fun addSMS() {
        if (!isFormEditable.get()!!) {
            _popBack.value = TriggeredEvent(true)
            return
        }

        if (validateInput()) {
            if (eventId == 0)
                scheduleSms()
            else
                updateSms()
        }
    }

    private fun updateSms() {
        viewModelScope.launch {

            val smsAndPhoneNumbers =
                scheduleRepository.getSmsAndPhoneNumbersWithEventId(eventId)
            val sms = smsAndPhoneNumbers.sms
            sms.message = etMessage.get().toString()
            sms.updatedAt = System.currentTimeMillis()
            sms.subscriptionID = getCheckedBoxId()
            scheduleRepository.updateSms(sms)

            for (phoneNumber in smsAndPhoneNumbers.phoneNumbers)
                scheduleRepository.deletePhoneNumber(phoneNumber)

            val phoneNumbers = arrayListOf<PhoneNumber>()
            for (number in receivers) {
                phoneNumbers.add(PhoneNumber(phoneNumber = number, smsID = sms.id))
            }
            scheduleRepository.insertPhoneNumbers(phoneNumbers)

            val event = scheduleRepository.getEventById(eventId)
            event.timestamp = selectDate.timeInMillis
            event.updatedAt = System.currentTimeMillis()
            scheduleRepository.updateEvent(event)

            alarmManager.updateAlarm(event.id, selectDate.timeInMillis)
            scheduleRepository.insertLog(
                EventLog(
                    logStatus = Constants.SMS_MODIFIED,
                    eventID = eventId
                )
            )

            showLoader.value = false
            showMessage.value = "Event Updated"
            popBack()
        }
    }

    private fun scheduleSms() {
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
                    message = etMessage.get().toString(),
                    subscriptionID = getCheckedBoxId()
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
            showMessage.value = "Event Added"
            popBack()

        }
    }

    private fun popBack(){
        _popBack.value = TriggeredEvent(true)
    }

    fun dismissAlarm() {
        alarmManager.dismissAlarm(eventId)

        viewModelScope.launch {
            val event = scheduleRepository.getEventById(eventId)
            event.status = Constants.DISMISSED
            event.updatedAt = System.currentTimeMillis()
            scheduleRepository.updateEvent(event)

            scheduleRepository.insertLog(
                EventLog(
                    logStatus = Constants.SMS_CANCELED,
                    eventID = event.id
                )
            )

            showLoader.value = false
            showMessage.value = "Event Cancelled"
            popBack()
        }
    }

    private fun validateInput(): Boolean {
        var isOkay = true
        hideAllError()
        if (receivers.isEmpty()) {
            receiverError.set(context.getString(R.string.error_empty))
            isOkay = false
        }

        if (etMessage.get().isNullOrEmpty()) {
            messageError.set(context.getString(R.string.error_empty))
            isOkay = false
        }
        return isOkay
    }

    private fun hideAllError() {
        receiverError.set(null)
        messageError.set(null)
    }

    fun setEventId(id: Int) {
        eventId = id
        // Fetch data to edit
        if (eventId != 0) {
            _actionBarText.value = "Edit SMS"
            try {
                viewModelScope.launch {
                    val event = scheduleRepository.getEventById(eventId)
                    if (event.status == Constants.PENDING || event.status == Constants.DISMISSED) {
                        showCancelButton.set(true)
                    }
                    val smsAndPhoneNumbers =
                        scheduleRepository.getSmsAndPhoneNumbersWithEventId(eventId)

                    receivers.clear()
                    for (phoneNumber in smsAndPhoneNumbers.phoneNumbers) {
                        receivers.add(phoneNumber.phoneNumber)
                    }
                    _receiverNumbers.value = receivers

                    etMessage.set(smsAndPhoneNumbers.sms.message)
                    selectDate.timeInMillis = event.timestamp
                    selectedDateText.set(Utils.dateFormatter.format(event.timestamp))
                    selectedTimeText.set(Utils.timeFormatter.format(event.timestamp))

                    setSimChecked(smsAndPhoneNumbers.sms.subscriptionID)
                }
            } catch (e: Exception) {
                showMessage.value = e.message
            }
        }
    }

    fun setIsEditable(isEditable: Boolean) {
        isFormEditable.set(isEditable)
        if (!isEditable)
            _actionBarText.value = "View SMS"
    }

    fun showSimCards() {
        val localSubscriptionManager = context.getSystemService(SubscriptionManager::class.java)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        addSimViews(localSubscriptionManager.activeSubscriptionInfoList)
    }

    private fun clearAllSims() {
        sims.clear()
    }

    private fun addSimView(checkBox: CheckBox) {
        checkBox.setOnCheckedChangeListener { v, isChecked ->
            onCheckBoxChecked(v.id, isChecked)
        }
        sims.add(checkBox)
    }

    private fun addSimViews(simInfoList: List<SubscriptionInfo>) {
        clearAllSims()
        for ((index, sim) in simInfoList.withIndex()) {
            val checkBox = CheckBox(context)
            val text = "SIM ${sim.simSlotIndex + 1} (${sim.displayName})"
            checkBox.text = text
            checkBox.id = sim.subscriptionId
            if (index == 0)
                checkBox.isChecked = true
            addSimView(checkBox)
        }

        _availableSims.value = sims

    }

    private fun onCheckBoxChecked(id: Int, isChecked: Boolean) {
        if (sims.size == 1)
            sims[0].isChecked = true
        for (sim in sims) {
            if (sim.id != id)
                sim.isChecked = !isChecked
        }
    }

    private fun getCheckedBoxId(): Int {
        for (sim in sims) {
            if (sim.isChecked)
                return sim.id
        }
        return 1
    }

    private fun setSimChecked(subscriptionID: Int) {
        onCheckBoxChecked(subscriptionID, true)
    }
}