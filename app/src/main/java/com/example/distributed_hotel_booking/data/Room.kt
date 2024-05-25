package com.example.distributed_hotel_booking.data

import java.math.BigDecimal

data class Room(
    val roomId: String,
    val roomName: String,
//    val description: String,
    val availableDateRange: DateRange,
    val noOfGuests: Int,
    val rating: BigDecimal,
    val noOfReviews: Int = 0,
    val roomImagePath: String = "",
    val bookings: List<Booking> = emptyList(),
//    val price: Float = 0f,
)

//fun bookingsOverlap(booking1: Booking, booking2: Booking): Boolean {
//    return booking1.checkInDate?.before(booking2.checkOutDate) == true && booking1.checkOutDate?.after(booking2.checkInDate) == true
//}
//fun bookingsIntersect(newBooking: Booking): Boolean {
//    for (booking in DataProvider.bookingsList) {
//        if (newBooking.roomId == booking.roomId && bookingsOverlap(newBooking, booking)) {
//            return true
//        }
//    }
//    return false
//}


