package org.backend.modules;

import org.backend.misc.DateRange;
import org.backend.misc.SearchFilter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.backend.misc.Util.imageToByteArray;

public class Room implements Serializable {
    public String roomId;
    private Manager manager;
    private final String roomName;
    private final Integer noOfGuests;
    private final String area;
    private BigDecimal rating;
    private final BigDecimal price;
    private Integer noOfReviews;
    private final DateRange availableDateRange;
    private final List<Booking> bookings;
    private final byte[] roomImage;

    public Room(String roomId, String roomName, Integer noOfGuests, String area, BigDecimal rating, Integer noOfReviews, BigDecimal price,
                String roomImagePath, DateRange availableDateRange) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.noOfGuests = noOfGuests;
        this.area = area;
        this.rating = rating;
        this.noOfReviews = noOfReviews;
        this.price = price;
        this.bookings = new ArrayList<>();
        this.roomImage = imageToByteArray(roomImagePath, 575, 400, 0.75f);
        this.availableDateRange = availableDateRange;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }

    public String getRoomId() {
        return roomId;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public String getRoomName(){
        return this.roomName;
    }

    public String getArea(){
        return this.area;
    }

    public Integer getNoOfGuests() { return this.noOfGuests;}

    public BigDecimal getStars() {return this.rating;}

    public byte[] getRoomImage() {
        return roomImage;
    }

    public DateRange getAvailableDateRange(){
        return  this.availableDateRange;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getNoOfReviews() {
        return noOfReviews;
    }

    public void addBooking(Booking booking){
        this.bookings.add(booking);
    }

    public void reviewOnCreate(Review review) {
        int grade = review.getRating();

        BigDecimal newReviewWeight = new BigDecimal(grade);
        BigDecimal oldTotalWeight = rating.multiply(new BigDecimal(noOfReviews));
        BigDecimal totalWeight = oldTotalWeight.add(newReviewWeight);

        if (noOfReviews > 0) {
            BigDecimal newStars = totalWeight.divide(new BigDecimal(noOfReviews + 1), 2, RoundingMode.HALF_UP);
            rating = newStars.setScale(2, RoundingMode.HALF_UP);
        } else {
            rating = newReviewWeight.setScale(2, RoundingMode.HALF_UP);
        }

        noOfReviews++;
    }

    public void reviewOnUpdate(Review oldReview, Review newReview) {
        BigDecimal oldTotalRating = rating.multiply(new BigDecimal(noOfReviews));
        BigDecimal newTotalRating = oldTotalRating.subtract(new BigDecimal(oldReview.getRating())).add(new BigDecimal(newReview.getRating()));
        rating = newTotalRating.divide(new BigDecimal(noOfReviews), 2, RoundingMode.HALF_UP);
    }

    public void reviewOnDelete(Review review) {
        BigDecimal oldTotalRating = rating.multiply(new BigDecimal(noOfReviews));
        BigDecimal newTotalRating = oldTotalRating.subtract(new BigDecimal(review.getRating()));
        noOfReviews--;
        if (noOfReviews > 0) {
            rating = newTotalRating.divide(new BigDecimal(noOfReviews), 2, RoundingMode.HALF_UP);
        } else {
            rating = BigDecimal.ZERO;
        }
    }

    public boolean book(Booking newBooking){
        Date bookingStartDate = newBooking.getDateRange().getStartDate();
        Date bookingEndDate = newBooking.getDateRange().getEndDate();
        if (bookingStartDate.before(this.availableDateRange.getStartDate())
                || bookingEndDate.after(this.availableDateRange.getEndDate())
        ) {
            return false;
        }

        for(Booking booking : bookings){
            if(newBooking.intersectingDates(booking) || newBooking.containsDate(booking)) {
                return false;
            }
        }
        this.addBooking(newBooking);

        return true;
    }

    public boolean meetsCriteria(SearchFilter filters) {
        // Check if the room name matches the one in the filter (exact match -> ignore case)
        boolean roomNameCheck = filters.getRoomName().isEmpty() || filters.getRoomName().compareToIgnoreCase(this.getRoomName()) == 0;
        // Check if the date range in the filter
        boolean dateRangeCheck = this.checkDateRange(filters.getDateRange(), this.getAvailableDateRange());
        // Check if the area in the filter is different from the room's area (exact match -> do not ignore case)
        boolean areaCheck = filters.getArea().isEmpty() || filters.getArea().compareToIgnoreCase(this.getArea()) == 0;
        // Check if the number of persons in the filter is less than or equal to the room's number of persons
        boolean personCountCheck = filters.getNoOfGuests() == 0 || (filters.getNoOfGuests()).compareTo(this.getNoOfGuests()) <= 0;
        // Check if the stars in the filter are less than or equal to the room's stars
        boolean starsCheck = filters.getRating().equals(new BigDecimal("0.0")) || filters.getRating().compareTo(this.getStars()) <= 0;
        // Return true if all the checks pass, false otherwise
        return roomNameCheck && dateRangeCheck && areaCheck && personCountCheck && starsCheck;
    }

    /* Checks if the Filter date range contains the room's available date range :
    Visualization: [filterStart , [roomStart , roomEnd] filterEnd]
    */
    private boolean checkDateRange(DateRange filterRange, DateRange roomRange){
        return DateRange.isAfterOrEqual(filterRange.getStartDate(), roomRange.getStartDate()) &&
                DateRange.isBeforeOrEqual(filterRange.getEndDate(), roomRange.getEndDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomName, room.roomName) && Arrays.equals(roomImage, room.roomImage);
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(roomName);
        return hash & Integer.MAX_VALUE; // Ensure hash is non-negative
    }

    @Override
    public String toString() {
        return STR."Room{roomName='\{roomName}\{'\''}, noOfGuests=\{noOfGuests}, area='\{area}\{'\''}, rating=\{rating}, noOfReviews=\{noOfReviews}, dateRange=\{availableDateRange}, bookings=\{bookings.toString()}\{'}'}";
    }
}
