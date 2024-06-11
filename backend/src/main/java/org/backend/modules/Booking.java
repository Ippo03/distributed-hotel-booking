package org.backend.modules;

import org.backend.misc.DateRange;
import org.backend.misc.RoomInfo;
import org.backend.user.UserData;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static java.lang.Math.round;

public class Booking implements Serializable {
    private final UserData userData;
    private final RoomInfo roomInfo;
    private final DateRange dateRange;
    private final float total;
    private Review review;

    public Booking(UserData userData, RoomInfo roomInfo, DateRange dateRange) {
        this.userData = userData;
        this.dateRange = dateRange;
        this.roomInfo = roomInfo;
        this.total = 0;
    }

    public boolean intersectingDates(Booking booking){
        return booking.getDateRange().intersects(this.getDateRange()) && booking.getRoomInfo().equals(this.getRoomInfo());
    }

    public boolean containsDate(Booking booking){
        return booking.getDateRange().isInside(this.getDateRange()) && booking.getRoomInfo().equals(this.getRoomInfo());
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public UserData getUserData() {
        return userData;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Review getReview() {
        return review;
    }

    @Override
    public int hashCode() {
        return this.getRoomInfo().hashCode();
    }

    @Override
    public String toString(){
        return STR."User with id #\{userData.getUserId()} has booked \{roomInfo.getRoomName()} \{this.dateRange.toBookingString()} for a total of \{total}€";
    }
}