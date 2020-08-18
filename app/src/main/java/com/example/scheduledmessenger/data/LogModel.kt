package com.example.scheduledmessenger.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import androidx.room.ForeignKey.CASCADE

@Entity(foreignKeys = [ForeignKey(entity = SMS::class, parentColumns = ["id"], childColumns = ["smsID"], onDelete = CASCADE)])
class LogModel {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var statusCode: Int = 0
    var smsID = 0
    var timeStamp: Long = 0
}
