package com.example.smsScheduler.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.smsScheduler.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Notification @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPrefManager: SharedPrefManager
) {
    private val notificationManager = NotificationManagerCompat.from(context)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channelID: String = Constants.NOTIFICATION_CHANNEL_ID
        val name: CharSequence = Constants.NOTIFICATION_CHANNEL_NAME
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(channelID, name, importance)
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(sharedPrefManager.getNotificationVibrationStatus())
        mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        mChannel.setShowBadge(false)
        notificationManager.createNotificationChannel(mChannel)
    }

    fun showNotification(title: String) {
        if (!sharedPrefManager.getNotificationStatus()) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.bottom_nav_graph)
            .setDestination(R.id.logFragment)
            .createPendingIntent()

        val notificationBuilder =
            NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setLights(0xff0000, 100, 100)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText("click to show")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)

        val notification = notificationBuilder.build()
        notification.contentIntent = pendingIntent
        notificationManager.notify(0, notification)
    }
}