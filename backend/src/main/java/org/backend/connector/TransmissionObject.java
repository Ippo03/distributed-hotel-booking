package org.backend.connector;

import org.backend.misc.DateRange;
import org.backend.misc.SearchFilter;
import org.backend.modules.Booking;
import org.backend.modules.Review;
import org.backend.modules.Room;
import org.backend.user.UserData;

import java.io.Serializable;
import java.util.List;

public class TransmissionObject {
    public TransmissionObjectType type;
    public String username;
    public String password;
    public UserData userData;
    public String message;
    public List<Room> rooms;
    public SearchFilter searchFilter;
    public Review review;
    public Booking booking;
    public int success;
    public List<Booking> userBookings;
    public DateRange bookingDates;

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

    public void setBooking(Booking booking) { this.booking = booking; }

    public void setRooms(List<Room> rooms) { this.rooms = rooms; }

    public void setUserBookings(List<Booking> userBookings) { this.userBookings = userBookings; }

    public void setBookingDates(DateRange bookingDates) { this.bookingDates = bookingDates; }
}