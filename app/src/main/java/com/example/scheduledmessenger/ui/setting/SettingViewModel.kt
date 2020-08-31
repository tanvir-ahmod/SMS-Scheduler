package com.example.scheduledmessenger.ui.setting

import android.os.Build
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import com.example.scheduledmessenger.base.BaseViewModel


class SettingViewModel @ViewModelInject constructor() :
    BaseViewModel() {
    val isShowAutoStart = ObservableField<Boolean>(false)

    init {
        if ("xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)) {
            isShowAutoStart.set(true)
        }
    }
}