package com.example.distributed_hotel_booking.data

import android.icu.math.BigDecimal
import android.util.Log
import java.math.RoundingMode
import java.time.temporal.ChronoUnit


data class Booking(
    var userId: Int,
    var room: Room?,
    var dateRange: DateRange?,
    var guests: Int?=1,
    var total: Float? = 0f
) {
    companion object {
        fun calculateTotal(booking: Booking): Float {
            Log.d("Booking", "Room price: ${booking.room?.price}")
            Log.d("Booking", "Number of days: ${booking.dateRange?.getDays()}")
            booking.total = (BigDecimal.valueOf(booking.room?.price?.toDouble() ?: 0.0)
                .multiply(BigDecimal.valueOf(booking.dateRange?.getDays()?.toLong() ?: 0L))
                .setScale(2)).toFloat()
            Log.d("Booking", "Total: ${booking.total}")
            return booking.total as Float
        }
    }
}

