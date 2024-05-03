package com.example.distributed_hotel_booking.data

import com.example.distributed_hotel_booking.data.DataProvider.roomsList

object DataProvider {
    val roomsList = listOf(
        Room("1", "Hilton Athens", "Luxury hotel with Acropolis views", 6),
        Room("2", "Grand Hyatt Athens", "Modern hotel near Plaka", 3),
        Room("3", "InterContinental Athenaeum", "Elegant hotel with rooftop pool", 5),
        Room("4", "Hotel Grande Bretagne", "Historic hotel with Michelin restaurants", 5),
        Room("5", "Electra Palace Athens", "Chic hotel with rooftop restaurant", 2),
        Room("6", "Athens Marriott Hotel", "Contemporary hotel with fitness center", 8),
        Room("7", "Wyndham Grand Athens", "Sleek hotel with panoramic views", 4),
        Room("8", "Divani Caravel Hotel", "Upscale hotel with multiple dining options", 3),
        Room("9", "Radisson Blu Park Hotel Athens", "Tranquil hotel with lush gardens", 3),
        Room("10", "Athenaeum Grand Hotel", "Modern hotel with rooftop terrace", 2)
    )

    fun getRoomById(roomId: String?): Room? {
        return roomsList.firstOrNull { it.id == roomId }
    }

}

