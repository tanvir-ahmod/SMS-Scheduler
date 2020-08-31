package com.example.scheduledmessenger.ui.setting

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import com.example.scheduledmessenger.base.BaseViewModel
import dagger.hilt.android.qualifiers.ApplicationContext


class SettingViewModel @ViewModelInject constructor(@ApplicationContext private val context: Context) :
    BaseViewModel() {
    val isShowAutoStart = ObservableField<Boolean>(false)
    val versionNumber = ObservableField<String>("")

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
}