package com.example.distributed_hotel_booking.data

data class Room(
    val id: String,
    val name: String,
    val description: String,
    val dateRange: DateRange,
    val guests: Int,
    val rating: Float = 0f,
    val noReviews: Int = 0,
    val imagePath: String = "",
    val price: Float = 0f,
)

fun bookingsOverlap(booking1: Booking, booking2: Booking): Boolean {
    return booking1.checkInDate?.before(booking2.checkOutDate) == true && booking1.checkOutDate?.after(booking2.checkInDate) == true
}
fun bookingsIntersect(newBooking: Booking): Boolean {
    for (booking in DataProvider.bookingsList) {
        if (newBooking.roomId == booking.roomId && bookingsOverlap(newBooking, booking)) {
            return true
        }
    }
    return false
}


