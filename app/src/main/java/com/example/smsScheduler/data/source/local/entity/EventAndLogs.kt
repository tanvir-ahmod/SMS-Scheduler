package com.example.smsScheduler.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class EventAndLogs(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "id",
        entityColumn = "event_id",
        entity = EventLog::class
    )
    val logs : List<EventLog>
)