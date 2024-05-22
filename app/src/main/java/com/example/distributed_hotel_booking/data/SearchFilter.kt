package com.example.distributed_hotel_booking.data

import java.math.BigDecimal

data class SearchFilter(
    val roomName : String,
    val dateRange: DateRange,
    val area: String,
    val noOfGuests: Int,
    val rating : BigDecimal, //Could be Int or IntRange
    val price: Int // Could be FloatRange
)
