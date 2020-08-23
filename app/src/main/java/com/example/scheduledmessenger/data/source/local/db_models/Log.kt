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
data class Log(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "event_id")
    var eventID: Int,

    @ColumnInfo(name = "log_message")
    var logMessage: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
