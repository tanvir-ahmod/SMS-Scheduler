package com.example.scheduledmessenger.data.source.local.db_models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "SMSs", foreignKeys = [androidx.room.ForeignKey(
        entity = Event::class,
        parentColumns = ["id"],
        childColumns = ["event_id"],
        onDelete = androidx.room.ForeignKey.CASCADE
    )]
)
data class SMS(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "event_id")
    val eventID: Int,

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)

