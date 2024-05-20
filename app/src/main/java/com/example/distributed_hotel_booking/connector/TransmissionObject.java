package com.example.distributed_hotel_booking.connector;

import com.example.distributed_hotel_booking.connector.user.UserData;
import com.example.distributed_hotel_booking.data.Room;
import com.example.distributed_hotel_booking.data.SearchFilter;

import java.util.List;

public class TransmissionObject {
    public TransmissionObjectType type;

    // Login
    public String username;
    public String password;

    public UserData userData;

    public String message;

    //public List<Room> rooms;

    public SearchFilter searchFilter;

    public int success;

    public void setType(TransmissionObjectType type) {
        this.type = type;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
    public void setSearchFilter(SearchFilter searchFilter) { this.searchFilter = searchFilter; }

    //public void setRooms(List<Room> rooms) { this.rooms = rooms; }
}
