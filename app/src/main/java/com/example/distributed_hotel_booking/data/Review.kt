package com.example.distributed_hotel_booking.data

data class Review(
    val rating: Int,
    val comment: String,
    var roomInfo:RoomInfo = RoomInfo("","")
)