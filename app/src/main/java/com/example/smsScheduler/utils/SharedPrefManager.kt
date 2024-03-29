package com.example.smsScheduler.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val SHOW_NOTIFICATION = "show_notification"
        private const val ENABLE_NOTIFICATION_VIBRATION = "enable_notification_vibration"
        private const val SHARED_PREF_NAME = "scheduled_messenger"
    }

    fun saveNotificationStatus(value: Boolean) {
        sharedPref.edit().putBoolean(SHOW_NOTIFICATION, value).apply()
    }

    fun getNotificationStatus(): Boolean = sharedPref.getBoolean(SHOW_NOTIFICATION, true)

    fun saveNotificationVibrationStatus(value: Boolean) {
        sharedPref.edit().putBoolean(ENABLE_NOTIFICATION_VIBRATION, value).apply()
    }

    fun getNotificationVibrationStatus(): Boolean = sharedPref.getBoolean(ENABLE_NOTIFICATION_VIBRATION, true)

}