package com.example.scheduledmessenger.ui.adapter

data class EventModel(
    val eventID : Int,
    val receivers: List<String>,
    val message: String,
    val timestamp: Long,
    val status: Int
)