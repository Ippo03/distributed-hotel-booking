package com.example.distributed_hotel_booking.data

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

data class Review(
    var userData: UserData = UserData(),
    val rating: Int,
    val comment: String,
    var roomInfo: RoomInfo = RoomInfo(),
    val date: Date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
)