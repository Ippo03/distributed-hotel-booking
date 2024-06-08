package com.example.distributed_hotel_booking.data

import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

data class Review(
    var userData: UserData = UserData(),
    val rating: Int,
    val comment: String,
    var roomInfo: RoomInfo = RoomInfo(),
    val date: Date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
)