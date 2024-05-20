package com.example.distributed_hotel_booking.connector;

public enum TransmissionObjectType {
    LOGIN,
    LOGOUT,
    SEARCH,
    GET_ALL_ROOMS, // get all rooms + to update rooms in the user's app + for rooms list
    GET_ALL_BOOKINGS,
    // get all of user's bookings (maybe just scan the rooms list if already have all rooms -> or immediately after Get_All_Rooms store them as myBookings in the user's app)

}
