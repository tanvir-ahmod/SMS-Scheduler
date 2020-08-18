package com.example.scheduledmessenger.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SMS {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var phoneNumber: String? = null
    var message: String? = null
    var timestamp: Long = 0
    var status = 0
}

