package com.example.distributed_hotel_booking.data

import java.util.Date

data class DateRange(
    val startDate: Date,
    val endDate: Date
) {
    fun getDays(): Int {
        val diff = endDate.time - startDate.time // milliseconds
        return (diff / (24 * 60 * 60 * 1000)).toInt() // days
    }

}
