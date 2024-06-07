package com.example.distributed_hotel_booking.data

data class Review(
    var userData: UserData = UserData(),
    val rating: Int,
    val comment: String,
    var roomInfo: RoomInfo = RoomInfo(),
)