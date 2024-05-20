package com.example.distributed_hotel_booking.connector;

import com.example.distributed_hotel_booking.connector.user.UserData;

public class TransmissionObjectBuilder {
    private TransmissionObject transmissionObject;

    public TransmissionObjectBuilder() {
        this.transmissionObject = new TransmissionObject();
    }

    public TransmissionObjectBuilder type(TransmissionObjectType type) {
        this.transmissionObject.setType(type);
        return this;
    }

    public TransmissionObjectBuilder userData(UserData userData) {
        this.transmissionObject.setUserData(userData);
        return this;
    }

    public TransmissionObjectBuilder message(String message) {
        this.transmissionObject.setMessage(message);
        return this;
    }

    public TransmissionObjectBuilder success(int success) {
        this.transmissionObject.setSuccess(success);
        return this;
    }

    public TransmissionObject build() {
        return this.transmissionObject;
    }
}