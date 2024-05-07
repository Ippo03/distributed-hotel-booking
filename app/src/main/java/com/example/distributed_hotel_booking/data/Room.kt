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
