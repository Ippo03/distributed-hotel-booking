package org.backend.misc;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DateRange implements Serializable {
    private final Date startDate;
    private final Date endDate;

    // Constructor
    public DateRange(Date startDate, Date endDate) {
        if ((startDate==null || endDate==null) || startDate.after(endDate) ){
            throw new IllegalArgumentException();
        } else {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }


    public boolean intersects(DateRange b){
        // Check if the start date of one range falls within the other range
        boolean startInRange = (b.startDate.after(this.startDate) || b.startDate.equals(this.startDate)) &&
                (b.startDate.before(this.endDate) || b.startDate.equals(this.endDate));

        // Check if the end date of one range falls within the other range
        boolean endInRange = (b.endDate.after(this.startDate) || b.endDate.equals(this.startDate)) &&
                (b.endDate.before(this.endDate) || b.endDate.equals(this.endDate));

        // Check if either start or end date of one range falls within the other range
        return startInRange || endInRange;
    }

    public boolean isInside(DateRange dateRange){
        return (this.startDate.after(dateRange.startDate) || this.startDate.equals(dateRange.startDate)) &&
                (this.endDate.before(dateRange.endDate) || this.endDate.equals(dateRange.endDate));
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRange dateRange = (DateRange) o;
        return Objects.equals(startDate, dateRange.startDate) && Objects.equals(endDate, dateRange.endDate);
    }

    /**
     * Checks if the given `date` is before or equal to the `compareToDate`.
     *
     * @param date          the date to be checked
     * @param compareToDate the date to compare against
     * @return true if the `date` is before or equal to the `compareToDate`, false otherwise
     */
    public static boolean isBeforeOrEqual(Date date, Date compareToDate) {
        if (date == null || compareToDate == null) {
            return false;
        }
        return date.before(compareToDate) || date.equals(compareToDate);
    }

    /**
     * Checks if the given `date` is after or equal to the `compareToDate`.
     *
     * @param date          the date to be checked
     * @param compareToDate the date to compare against
     * @return true if the `date` is after or equal to the `compareToDate`, false otherwise
     */
    public static boolean isAfterOrEqual(Date date, Date compareToDate) {
        if (date == null || compareToDate == null) {
            return false;
        }
        return date.after(compareToDate) || date.equals(compareToDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    // Utility method to get the minimum possible date
    public static Date getMinDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1900);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    // Utility method to get the maximum possible date
    public static Date getMaxDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 9999);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        String startDateStr = dateFormat.format(startDate);
        String endDateStr = dateFormat.format(endDate);
        return STR."DateRange{startDate=\{startDateStr}, endDate=\{endDateStr}\{'}'}";
    }

    public String toBookingString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        String startDateStr = dateFormat.format(startDate);
        String endDateStr = dateFormat.format(endDate);
        return STR."from \{startDateStr} to \{endDateStr}";
    }

    public String toRangeString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        String startDateStr = dateFormat.format(startDate);
        String endDateStr = dateFormat.format(endDate);
        return STR."\{startDateStr} - \{endDateStr}";
    }
}

