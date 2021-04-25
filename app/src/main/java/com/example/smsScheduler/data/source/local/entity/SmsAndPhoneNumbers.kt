package com.example.smsScheduler.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SmsAndPhoneNumbers(
    @Embedded val sms: SMS,

    @Relation(
        parentColumn = "id",
        entityColumn = "sms_id"
    )

    val phoneNumbers: List<PhoneNumber>
)
