package com.example.distributed_hotel_booking.data

import java.util.Objects

class RoomInfo(var roomId: String = "", var roomName: String = "", var roomImage: ByteArray = ByteArray(0)) {

    override fun hashCode(): Int {
        val hash = Objects.hash(roomName)
        return hash and Int.MAX_VALUE
    }

    override fun toString(): String {
        return "RoomInfo{" +
                "roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                '}'
    }
}