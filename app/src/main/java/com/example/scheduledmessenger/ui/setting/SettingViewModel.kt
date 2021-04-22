package com.example.scheduledmessenger.ui.setting

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.util.Log
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import com.example.scheduledmessenger.base.BaseViewModel
import com.example.scheduledmessenger.utils.SharedPrefManager
import dagger.hilt.android.qualifiers.ApplicationContext


class SettingViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val isShowAutoStart = ObservableField<Boolean>(false)
    val versionNumber = ObservableField<String>("")
    val isNotificationChecked = ObservableField<Boolean>(sharedPrefManager.getNotificationStatus())
    val isNotificationVibrationChecked = ObservableField<Boolean>(sharedPrefManager.getNotificationVibrationStatus())

    init {
        setVersionNumber()
        verifyAutoStartEnableSetting()
    }

    private fun verifyAutoStartEnableSetting() {
        if ("xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)) {
            isShowAutoStart.set(true)
        }
    }

    private fun setVersionNumber() {
        val pInfo: PackageInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)
        versionNumber.set(pInfo.versionName)
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