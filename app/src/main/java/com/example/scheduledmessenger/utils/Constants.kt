package com.example.scheduledmessenger.utils


object Constants {
    const val PENDING = 0
    const val SENT = 1
    const val FAILED = 2
    const val DISMISSED = 3

    const val SMS_INITIATED = 1
    const val SMS_SENT = 2
    const val SMS_MODIFIED = 3
    const val SMS_CANCELED = 4

    val LOG_MESSAGE: HashMap<Int, String> =
        hashMapOf(
            SMS_INITIATED to "SMS initiated",
            SMS_SENT to "SMS sent",
            SMS_MODIFIED to "SMS modified",
            SMS_CANCELED to "SMS Cancelled"
        )
}