package com.example.smsScheduler.ui.settings

import android.os.Build
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import com.example.smsScheduler.base.BaseViewModel
import com.example.smsScheduler.utils.SharedPrefManager


class SettingsViewModel @ViewModelInject constructor(
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val isShowAutoStart = ObservableField<Boolean>(false)
    val isNotificationChecked = ObservableField<Boolean>(sharedPrefManager.getNotificationStatus())
    val isNotificationVibrationChecked =
        ObservableField<Boolean>(sharedPrefManager.getNotificationVibrationStatus())

    init {
        verifyAutoStartEnableSetting()
    }

    private fun verifyAutoStartEnableSetting() {
        if ("xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)) {
            isShowAutoStart.set(true)
        }
    }

    fun onNotificationCheckedChanged(value: Boolean) {
        isNotificationChecked.set(value)
        sharedPrefManager.saveNotificationStatus(value)
    }

    fun onNotificationVibrationCheckedChanged(value: Boolean) {
        isNotificationVibrationChecked.set(value)
        sharedPrefManager.saveNotificationVibrationStatus(value)
    }
}