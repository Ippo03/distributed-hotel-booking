package com.example.distributed_hotel_booking.data

import com.example.distributed_hotel_booking.util.parseDate
import java.math.BigDecimal
import java.util.Date

object DataProvider {
//    val roomsList = mutableListOf<Room>(
//        Room(
//            "1",
//            "Hilton Athens",
//            DateRange(parseDate("20-05-2024"), parseDate("25-06-2024")),
//            1,
//            BigDecimal.ZERO,
//        )
//    )
//        Room(
//            "1",
//            "Hilton Athens",
//            "Luxury hotel with Acropolis views",
//            DateRange(parseDate("20-05-2024"), parseDate("25-06-2024")),
//            1,
//            price = 200f,
//            rating = 4.5f,
//        ),
//        Room(
//            "2",
//            "Grand Hyatt Athens",
//            "Modern hotel near Plaka",
//            DateRange(parseDate("10-06-2024"), parseDate("15-07-2024")),
//            3,
//            price = 150f,
//            rating = 4.0f,
//        ),
//        Room(
//            "3",
//            "InterContinental Athenaeum",
//            "Elegant hotel with rooftop pool",
//            DateRange(parseDate("05/07/2024"), parseDate("10/08/2024")),
//            4,
//            price = 250f,
//            rating = 3.0f
//        ),
//        Room(
//            "4",
//            "Hotel Grande Bretagne",
//            "Historic hotel with Michelin restaurants",
//            DateRange(parseDate("15/08/2024"), parseDate("20/09/2024")),
//            1,
//            price = 200f,
//            rating = 2.8f
//        ),
//        Room(
//            "5",
//            "Electra Palace Athens",
//            "Chic hotel with rooftop restaurant",
//            DateRange(parseDate("01/09/2024"), parseDate("05/10/2024")),
//            2,
//            price = 350f,
//            rating = 3.2f
//        ),
//        Room(
//            "6",
//            "Athens Marriott Hotel",
//            "Contemporary hotel with fitness center",
//            DateRange(parseDate("10/10/2024"), parseDate("15/11/2024")),
//            4,
//            price = 280f
//        ),
//        Room(
//            "7",
//            "Wyndham Grand Athens",
//            "Sleek hotel with panoramic views",
//            DateRange(parseDate("20/11/2024"), parseDate("25/12/2024")),
//            4,
//            price = 260f
//        ),
//        Room(
//            "8",
//            "Divani Caravel Hotel",
//            "Upscale hotel with multiple dining options",
//            DateRange(parseDate("05/12/2024"), parseDate("10/01/2025")),
//            3,
//            price = 500f
//        ),
//        Room(
//            "9",
//            "Radisson Blu Park Hotel Athens",
//            "Tranquil hotel with lush gardens",
//            DateRange(parseDate("15/01/2025"), parseDate("20/02/2025")),
//            3,
//            price = 450f
//        ),
//        Room(
//            "10",
//            "Athenaeum Grand Hotel",
//            "Modern hotel with rooftop terrace",
//            DateRange(parseDate("01/02/2025"), parseDate("05/03/2025")),
//            2,
//            price = 300f
//        ),
//    )

//    val reviewsList = mutableListOf<Review>(
//        Review("1", roomsList[0], 5, "Great hotel, amazing views!"),
//        Review("2", roomsList[0], 4, "Good location, friendly staff"),
//        Review("3", roomsList[0], 3, "Nice hotel, but a bit noisy"),
//        Review("4", roomsList[0], 4, "Clean rooms, good breakfast"),
//        Review("5", roomsList[0], 5, "Excellent service, beautiful pool"),
//        Review("6", roomsList[0], 4, "Spacious rooms, great location"),
//        Review("7", roomsList[0], 5, "Luxurious hotel, excellent food"),
//        Review("8", "4", 4, "Helpful staff, comfortable beds"),
//        Review("9", "5", 5, "Stunning views, delicious food"),
//        Review("10", "5", 4, "Modern rooms, great location"),
//        Review("11", "6", 5, "Fantastic hotel, friendly staff"),
//        Review("12", "6", 4, "Clean rooms, good facilities"),
//        Review("13", "7", 5, "Amazing views, comfortable rooms"),
//        Review("14", "7", 4, "Great location, friendly staff"),
//        Review("15", "8", 5, "Luxurious hotel, excellent food"),
//        Review("16", "8", 4, "Helpful staff, comfortable beds"),
//        Review("17", "9", 5, "Stunning views, delicious food"),
//        Review("18", "9", 4, "Modern rooms, great location"),
//        Review("19", "10", 5, "Fantastic hotel, friendly staff"),
//        Review("20", "10", 4, "Clean rooms, good facilities"),
//    )

//    val bookingsList = mutableListOf<Booking>(
//        Booking("1", "1", Date(2024, 5, 20), Date(2024, 5, 25), 2, 400f),
//        Booking("1", "2", Date(2024, 6, 10), Date(2024, 6, 15), 3, 450f),
//        Booking("1", "3", Date(2024, 7, 5), Date(2024, 7, 10), 4, 1000f),
//        Booking("1", "4", Date(2024, 8, 15), Date(2024, 8, 20), 1, 200f),
//        Booking("1", "5", Date(2024, 9, 1), Date(2024, 9, 5), 2, 700f),
//        Booking("2", "6", Date(2024, 10, 10), Date(2024, 10, 15), 4, 1120f),
//        Booking("2", "7", Date(2024, 11, 20), Date(2024, 11, 25), 4, 1040f),
//        Booking("2", "8", Date(2024, 12, 5), Date(2024, 12, 10), 3, 1500f),
//        Booking("2", "9", Date(2025, 1, 15), Date(2025, 1, 20), 3, 1350f),
//        Booking("2", "10", Date(2025, 2, 1), Date(2025, 2, 5), 2, 600f),
//    )

//    fun addRoom(newRoom: Room) {
//        roomsList.add(newRoom)
//    }
//
//    fun getRoomById(roomId: String?): Room? {
//        return roomsList.firstOrNull { it.roomId == roomId }
//    }
//
//    fun getRoomByName(roomName: String): Room? {
//        return roomsList.firstOrNull { it.roomName == roomName }
//    }
//
//    fun getBookingsByRoomId(roomId: String): List<Booking> {
//        return bookingsList.filter { it.roomId == roomId }
//    }
//
//    fun getBookingsByDateRange(checkInDate: Date, checkOutDate: Date): List<Booking> {
//        return bookingsList.filter {
//            (it.checkInDate?.before(checkOutDate) ?: false) && (it.checkOutDate?.after(checkInDate)
//                ?: false)
//        }
//    }
//
//    fun getReviewsByRoomId(roomId: String): List<Review> {
//        return reviewsList.filter { it.room.roomId == roomId }
//    }
//
//    fun getBookingsByUserId(userId: String): List<Booking> {
//        return bookingsList.filter { it.userId == userId }
//    }
//
//    fun addBooking(booking: Booking): Boolean {
//        if (bookingsIntersect(booking)) {
//            return false
//        } else {
//            bookingsList.add(booking)
//            return true
//        }
//    }
//
//    fun updateRoomsList(rooms: List<Room>) {
//        roomsList.clear()
//        roomsList.addAll(rooms)
//    }
}

