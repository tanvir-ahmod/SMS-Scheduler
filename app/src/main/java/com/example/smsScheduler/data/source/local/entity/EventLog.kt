package com.example.smsScheduler.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Event::class,
        parentColumns = ["id"],
        childColumns = ["event_id"],
        onDelete = CASCADE
    )]
)
data class EventLog(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "event_id")
    var eventID: Int,

    @ColumnInfo(name = "log_message")
    var logStatus: Int,

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = System.currentTimeMillis()

)
