package com.example.scheduledmessenger.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    val timelineDateFormatter = SimpleDateFormat("dd MMM, yyyy hh:mm a", Locale.ENGLISH)
    val logDateFormatter = SimpleDateFormat("dd MMM", Locale.ENGLISH)
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
}