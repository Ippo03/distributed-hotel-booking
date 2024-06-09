package com.example.distributed_hotel_booking.util

import android.util.Log
import com.example.distributed_hotel_booking.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

fun getResourceId(resourceId: String): Int {
    return when (resourceId) {
        "1" -> R.drawable.guests_1
        "2" -> R.drawable.guests_2
        "3" -> R.drawable.guests_3
        "4" -> R.drawable.guests_4
        else -> throw IllegalArgumentException("Invalid resource ID: $resourceId")
    }
}

fun getTimeAgo(date: Date): String {
    val now = Date()
    Log.d("Now", now.toString())
    Log.d("Date", date.toString())
    val diffInMillis = now.time - date.time

    Log.d("Diff in millis", diffInMillis.toString())

    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

    Log.d("Minutes", minutes.toString())
    Log.d("Hours", hours.toString())
    Log.d("Days", days.toString())

    return when {
        minutes < 1 -> "just now"
        minutes < 60 -> "$minutes minute${if (minutes == 1L) "" else "s"} ago"
        hours < 24 -> "$hours hour${if (hours == 1L) "" else "s"} ago"
        else -> "$days day${if (days == 1L) "" else "s"} ago"
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

