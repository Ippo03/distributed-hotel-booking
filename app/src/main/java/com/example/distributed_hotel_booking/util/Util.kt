package com.example.distributed_hotel_booking.util

import com.example.distributed_hotel_booking.R

fun getResourceId(resourceId: String): Int {
    return when (resourceId) {
        "1" -> R.drawable.guests_1
        "2" -> R.drawable.guests_2
        "3" -> R.drawable.guests_3
        "4" -> R.drawable.guests_4
        else -> throw IllegalArgumentException("Invalid resource ID: $resourceId")
    }
}