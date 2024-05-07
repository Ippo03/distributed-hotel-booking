package com.example.distributed_hotel_booking.data

data class Review (
    val userId : String,
    val roomId : String,
    val rating : Int,
    val comment : String,
)