package com.example.distributed_hotel_booking.connector;

import com.example.distributed_hotel_booking.connector.user.UserData;
import com.example.distributed_hotel_booking.data.Booking;
import com.example.distributed_hotel_booking.data.Review;
import com.example.distributed_hotel_booking.data.SearchFilter;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

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

    public TransmissionObjectBuilder searchFilter(SearchFilter searchFilter) {
        this.transmissionObject.setSearchFilter(searchFilter);
        return this;
    }

    public TransmissionObjectBuilder review(Review review) {
        this.transmissionObject.setReview(review);
        return this;
    }

    public TransmissionObjectBuilder booking(Booking booking) {
        this.transmissionObject.setBooking(booking);
        return this;
    }

    public TransmissionObject build() {
        return this.transmissionObject;
    }
}