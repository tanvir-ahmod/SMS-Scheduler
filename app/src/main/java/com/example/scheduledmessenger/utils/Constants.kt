package com.example.scheduledmessenger.utils


object Constants {
    const val ID = "id"
    const val PENDING = 0
    const val SENT = 1
    const val FAILED = 2
    const val DISMISSED = 3

    const val SMS_INITIATED = 1
    const val SMS_SENT = 2
    const val SMS_MODIFIED = 3
    const val SMS_CANCELED = 4
    const val SMS_FAILED = 5

    val LOG_MESSAGE: HashMap<Int, String> =
        hashMapOf(
            SMS_INITIATED to "SMS initiated",
            SMS_SENT to "SMS sent",
            SMS_MODIFIED to "SMS modified",
            SMS_CANCELED to "SMS dismissed",
            SMS_FAILED to "SMS failed"
        )

    val EVENT_STATUS: HashMap<Int, String> =
        hashMapOf(
            PENDING to "PENDING",
            SENT to "SENT",
            SMS_MODIFIED to "SMS modified",
            FAILED to "FAILED",
            DISMISSED to "DISMISSED"
        )
}