package org.backend.connector;

import org.backend.misc.DateRange;
import org.backend.misc.SearchFilter;
import org.backend.modules.Booking;
import org.backend.modules.Review;
import org.backend.modules.Room;
import org.backend.user.UserData;

import java.util.List;

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

    public TransmissionObjectBuilder searchFilter(SearchFilter searchFilter) {
        this.transmissionObject.setSearchFilter(searchFilter);
        return this;
    }

    public TransmissionObjectBuilder review(Review review) {
        this.transmissionObject.setReview(review);
        return this;
    }

    public TransmissionObjectBuilder userBookings( List<Booking> bookings){
        this.transmissionObject.setUserBookings(bookings);
        return this;
    }

    public TransmissionObjectBuilder bookingDates(DateRange bookingDates) {
        this.transmissionObject.setBookingDates(bookingDates);
        return this;
    }

    public TransmissionObjectBuilder rooms(List<Room> rooms) {
        this.transmissionObject.setRooms(rooms);
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