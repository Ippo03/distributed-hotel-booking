package com.example.distributed_hotel_booking.data

data class Review(
    val userId: Int,
    val room: Room?,
    val rating: Int,
    val comment: String,
)