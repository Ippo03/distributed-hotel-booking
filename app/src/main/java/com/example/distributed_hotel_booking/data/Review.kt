package com.example.distributed_hotel_booking.data

import com.example.distributed_hotel_booking.connector.user.UserData

data class Review(
    val rating: Int,
    val comment: String,
    var roomInfo: RoomInfo = RoomInfo("",""),
)