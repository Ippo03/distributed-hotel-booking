package com.example.distributed_hotel_booking.data;

import java.util.Objects

class UserData(var userId: Int = 0, var username: String = "", var profilePicture: ByteArray = ByteArray(0)) {

override fun hashCode(): Int {
    val hash = Objects.hash(username)
    return hash and Int.MAX_VALUE
}

override fun toString(): String {
    return "UserData{" +
            "id=" + userId +
            ", username='" + username + '\'' +
            '}'
}
}
