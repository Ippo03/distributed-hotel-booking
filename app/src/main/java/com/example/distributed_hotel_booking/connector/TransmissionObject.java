package com.example.distributed_hotel_booking.connector;

import com.example.distributed_hotel_booking.data.UserData;
import com.example.distributed_hotel_booking.data.Booking;
import com.example.distributed_hotel_booking.data.DateRange;
import com.example.distributed_hotel_booking.data.Review;
import com.example.distributed_hotel_booking.data.Room;
import com.example.distributed_hotel_booking.data.SearchFilter;
import com.google.gson.Gson;

import java.util.List;

public class TransmissionObject {
    //General
    public TransmissionObjectType type;
    public String message;

    public int success;
    // Login
    public String username;
    public String password;
    public UserData userData;
    // Search
    public List<Room> rooms;
    public SearchFilter searchFilter;
    // Review
    public Review review;
    // Booking
    public Booking booking;
    // User's Bookings
    public List<Booking> userBookings;

    public DateRange bookingDates;
    // User's Reviews
    public List<Review> userReviews;

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

    public void setReview(Review review) { this.review = review; }

    public void setRooms(List<Room> rooms) { this.rooms = rooms; }

    public void setBooking(Booking booking) { this.booking = booking; }

    public void setBookingDates(DateRange bookingDates) { this.bookingDates = bookingDates; }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
