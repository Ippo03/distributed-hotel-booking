package com.example.distributed_hotel_booking.data

import java.math.BigDecimal

data class Room(
    val roomId: String,
    val roomName: String,
    val availableDateRange: DateRange,
    val noOfGuests: Int,
    val rating: BigDecimal,
    val price: Float,
    val noOfReviews: Int = 0,
    val roomImage: ByteArray = ByteArray(0),
    var bookings: List<Booking> = emptyList()

) {
    override fun toString(): String {
        return "Room(roomId='$roomId', roomName='$roomName', availableDateRange=$availableDateRange, noOfGuests=$noOfGuests, rating=$rating, price=$price, noOfReviews=$noOfReviews, bookings=$bookings)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Room

        if (roomId != other.roomId) return false
        if (roomName != other.roomName) return false
        if (availableDateRange != other.availableDateRange) return false
        if (noOfGuests != other.noOfGuests) return false
        if (rating != other.rating) return false
        if (price != other.price) return false
        if (noOfReviews != other.noOfReviews) return false
        if (!roomImage.contentEquals(other.roomImage)) return false
        return bookings == other.bookings
    }

    override fun hashCode(): Int {
        var result = roomId.hashCode()
        result = 31 * result + roomName.hashCode()
        result = 31 * result + availableDateRange.hashCode()
        result = 31 * result + noOfGuests
        result = 31 * result + rating.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + noOfReviews
        result = 31 * result + roomImage.contentHashCode()
        result = 31 * result + bookings.hashCode()
        return result
    }
}


