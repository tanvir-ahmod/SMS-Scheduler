package com.example.smsScheduler.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsManager
import android.widget.Toast
import com.example.smsScheduler.data.source.ScheduleRepository
import com.example.smsScheduler.data.source.local.entity.EventLog
import com.example.smsScheduler.data.source.local.entity.PhoneNumber
import com.example.smsScheduler.utils.Constants.LOG_MESSAGE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SMSSender @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scheduleRepository: ScheduleRepository,
    private val notification: Notification
) {
    private val SENT = "SMS_SENT"
    private val DELIVERED = "SMS_DELIVERED"

    private fun sendSms(subscriptionId: Int, phoneNumbers: List<PhoneNumber>, message: String) {
        for (phoneNumber in phoneNumbers) {
            send(subscriptionId, phoneNumber.phoneNumber, message)
        }
    }

    fun sendSms(eventId: Int) {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                val smsAndPhoneNumbers =
                    scheduleRepository.getSmsAndPhoneNumbersWithEventId(eventId)
                sendSms(
                    smsAndPhoneNumbers.sms.subscriptionID,
                    smsAndPhoneNumbers.phoneNumbers,
                    smsAndPhoneNumbers.sms.message
                )

                notification.showNotification(LOG_MESSAGE[Constants.SMS_SENT]!!)
                updateDbWithSuccess(eventId)
            }
        } catch (e: Exception) {
            GlobalScope.launch {
                val event = scheduleRepository.getEventById(eventId)
                event.status = Constants.FAILED

                scheduleRepository.updateEvent(event)
                scheduleRepository.insertLog(
                    EventLog(
                        logStatus = Constants.SMS_FAILED,
                        eventID = eventId
                    )
                )
                notification.showNotification(LOG_MESSAGE[Constants.SMS_FAILED]!!)
            }
        }
    }

    private suspend fun updateDbWithSuccess(eventId: Int) {
        val event = scheduleRepository.getEventById(eventId)
        event.status = Constants.SENT

        scheduleRepository.updateEvent(event)
        scheduleRepository.insertLog(
            EventLog(
                logStatus = Constants.SMS_SENT,
                eventID = eventId
            )
        )
    }

    private fun send(subscriptionId: Int, phoneNumber: String, message: String) {

        val sentPI = PendingIntent.getBroadcast(
            context, 0,
            Intent(SENT), 0
        )
        val deliveredPI = PendingIntent.getBroadcast(
            context, 0,
            Intent(DELIVERED), 0
        )

        //---when the SMS has been sent---
        /*context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent?) {
                when (resultCode) {
                    Activity.RESULT_OK -> Toast.makeText(
                        context, "SMS sent",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(
                        context, "Generic failure",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(
                        context, "No service",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_NULL_PDU -> Toast.makeText(
                        context, "Null PDU",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_RADIO_OFF -> Toast.makeText(
                        context, "Radio off",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }, IntentFilter(SENT))*/

        //---when the SMS has been delivered---
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent?) {
                when (resultCode) {
                    Activity.RESULT_OK -> Toast.makeText(
                        context, "SMS delivered",
                        Toast.LENGTH_SHORT
                    ).show()
                    Activity.RESULT_CANCELED -> Toast.makeText(
                        context, "SMS not delivered",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }, IntentFilter(DELIVERED))

        SmsManager.getSmsManagerForSubscriptionId(subscriptionId)
            .sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)
        /* val sms = SmsManager.getDefault()
         sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)*/
    }
}