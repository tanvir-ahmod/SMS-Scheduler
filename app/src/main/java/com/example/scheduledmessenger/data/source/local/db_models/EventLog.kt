package com.example.scheduledmessenger.data.source.local.db_models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import androidx.room.ForeignKey.CASCADE

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

    @ColumnInfo(name = "created_at")
    var createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    var updatedAt: Long = System.currentTimeMillis()
)
