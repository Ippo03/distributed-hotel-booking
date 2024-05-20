package com.example.distributed_hotel_booking.data

data class SearchFilter(
    val title : String,
    val dateRange: DateRange,
    val area: String,
    val numberOfGuests: Int,
    val rating : Float, //Could be Int or IntRange
    val priceRange: Int // Could be FloatRange
)
