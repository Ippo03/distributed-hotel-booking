package com.example.distributed_hotel_booking.util

import android.util.Log
import com.example.distributed_hotel_booking.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

fun getResourceId(resourceId: String): Int {
    return when (resourceId) {
        "1" -> R.drawable.guests_1
        "2" -> R.drawable.guests_2
        "3" -> R.drawable.guests_3
        "4" -> R.drawable.guests_4
        else -> throw IllegalArgumentException("Invalid resource ID: $resourceId")
    }
}

fun parseDate(dateStr: String): Date {
    val format = SimpleDateFormat("dd-MM-yyyy")
    Log.d("Formatted date", format.parse(dateStr).toString())
    return format.parse(dateStr)
}

fun getToday(): Date {
    return Calendar.getInstance().time
}

// Function to get the max date, e.g., one year from today
fun getMaxDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.YEAR, 10)
    return calendar.time
}

