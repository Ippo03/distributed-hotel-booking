package com.example.distributed_hotel_booking.data

import java.math.BigDecimal

data class Room(
    val roomId: String,
    val roomName: String,
//    val description: String,
    val availableDateRange: DateRange,
    val noOfGuests: Int,
    val rating: BigDecimal,
    val price: Float,
    val noOfReviews: Int = 0,
    val roomImagePath: String = "",
    val bookings: List<Booking> = emptyList()
) {
    override fun toString(): String {
        return "Room(roomId='$roomId', roomName='$roomName', availableDateRange=$availableDateRange, noOfGuests=$noOfGuests, rating=$rating, price=$price, noOfReviews=$noOfReviews, roomImagePath='$roomImagePath', bookings=$bookings)"
    }
}


