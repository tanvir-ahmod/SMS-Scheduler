package com.example.scheduledmessenger.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class EventWithSmsAndPhoneNumbers(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "id",
        entityColumn = "event_id",
        entity = SMS::class
    )
    val smsAndPhoneNumbers: SmsAndPhoneNumbers
)