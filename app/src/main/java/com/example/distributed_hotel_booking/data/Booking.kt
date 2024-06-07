package com.example.distributed_hotel_booking.data

import android.icu.math.BigDecimal
import android.util.Log


data class Booking(
    var userData: UserData?,
    var roomInfo: RoomInfo?,
    var dateRange: DateRange?,
    var guests: Int? = 1,
    var total: Float? = 0f,
    var review : Review? = null
) {
    companion object {
        fun calculateTotal(booking: Booking, roomPrice: Float): Float {
            Log.d("Booking", "Number of days: ${booking.dateRange?.getDays()}")
            booking.total = (BigDecimal.valueOf(roomPrice.toDouble() ?: 0.0)
                .multiply(BigDecimal.valueOf(booking.dateRange?.getDays()?.toLong() ?: 0L))
                .setScale(2)).toFloat()
            Log.d("Booking", "Total: ${booking.total}")
            return booking.total as Float
        }
    }
}

