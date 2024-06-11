package org.backend.misc;

import java.io.Serializable;
import java.math.BigDecimal;

public class SearchFilter implements Serializable {
    private final String roomName;
    private final Integer noOfGuests;
    private final String area;
    private final BigDecimal rating;
    private final DateRange dateRange;
    private final Integer price;

    public SearchFilter(String roomName, Integer noOfGuests, String area, BigDecimal rating, DateRange dateRange, Integer price) {
        this.roomName = roomName;
        this.noOfGuests = noOfGuests;
        this.area = area;
        this.rating = rating;
        this.dateRange = dateRange;
        this.price = price;
    }

    public String getRoomName() {
        return roomName;
    }

    public Integer getNoOfGuests() {
        return noOfGuests;
    }

    public String getArea() {
        return area;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public Integer getPrice() { return price; }

    @Override
    public String toString() {
        return STR."SearchFilter{roomName='\{roomName}\{'\''}, noOfPersons=\{noOfGuests}, area='\{area}\{'\''}, rating=\{rating}, dateRange=\{dateRange}\{'}'}";
    }
}
