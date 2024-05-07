package com.example.distributed_hotel_booking.data

import java.util.Date

data class Booking (
    val userId: String,
    val roomId: String,
    val checkInDate: Date?,
    val checkOutDate: Date?,
    val guests: Int?=1,
    val total: Float? = 0f
)