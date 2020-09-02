package com.example.scheduledmessenger.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val SHOW_NOTIFICATION = "show_notification"
        private const val SHARED_PREF_NAME = "scheduled_messenger"
    }

    fun saveNotificationStatus(value: Boolean) {
        sharedPref.edit().putBoolean(SHOW_NOTIFICATION, value).apply()
    }

    fun getNotificationStatus(): Boolean = sharedPref.getBoolean(SHOW_NOTIFICATION, true)

}