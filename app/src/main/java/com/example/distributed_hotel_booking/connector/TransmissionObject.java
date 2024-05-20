package com.example.distributed_hotel_booking.connector;

import com.example.distributed_hotel_booking.connector.user.UserData;

public class TransmissionObject {
    public TransmissionObjectType type;

    // Login
    public String username;
    public String password;

    public UserData userData;

    public String message;
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
}
