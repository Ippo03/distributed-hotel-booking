package org.backend.misc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Map;

import org.backend.misc.DateRange;
import org.backend.modules.Room;

public class JsonParser {
    public List<Room> roomsFromJson(String file) {
        List<Room> rooms = new ArrayList<>();
        String content;

        try {
            content = new String(Files.readAllBytes(Paths.get(file)));

            JSONArray array = new JSONArray(content);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String roomId = obj.getString("roomId");
                String roomName = obj.getString("roomName");
                int noOfPersons = obj.getInt("noOfPersons");
                String area = obj.getString("area");
                BigDecimal stars = obj.getBigDecimal("stars");
                BigDecimal price = obj.getBigDecimal("price");
                int noOfReviews = obj.getInt("noOfReviews");
                String roomImage = obj.getString("roomImage");

                // Parse the availableDates JSONArray and create a DateRange object
                DateRange dateRange = parseAvailableDates(obj.getJSONArray("availableDates"));

                Room r = new Room(roomId, roomName, noOfPersons, area, stars, noOfReviews, price, roomImage, dateRange);
                rooms.add(r);
            }
        } catch (IOException e) {
            System.out.println("json file not found");

        } catch (JSONException | ParseException e) {
            System.out.print("JSON ERROR PLEASE CONFIGURE FILE ");
        }
        return rooms;
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public DateRange parseAvailableDates(JSONArray availableDatesArray) throws JSONException, ParseException {
        if (availableDatesArray.isEmpty()) {
            return new DateRange(DateRange.getMinDate(), DateRange.getMaxDate());
        }

        Date from = null;
        Date to = null;

        for (int i = 0; i < availableDatesArray.length(); i++) {
            JSONObject dateObject = availableDatesArray.getJSONObject(i);
            String startDateStr = dateObject.optString("startDate", "");
            String endDateStr = dateObject.optString("endDate", "");

            Date startDate = null;
            Date endDate = null;

            if (!startDateStr.isEmpty()) {
                startDate = DATE_FORMAT.parse(startDateStr);
            }

            if (!endDateStr.isEmpty()) {
                endDate = DATE_FORMAT.parse(endDateStr);
            }

            // Adjust from and to dates based on parsed values
            if (startDate != null && (from == null || startDate.before(from))) {
                from = startDate;
            }

            if (endDate != null && (to == null || endDate.after(to))) {
                to = endDate;
            }
        }

        // Set default values if no dates are found
        if (from == null) {
            from = DateRange.getMinDate();
        }
        if (to == null) {
            to = DateRange.getMaxDate();
        }

        return new DateRange(from, to);
    }
}
