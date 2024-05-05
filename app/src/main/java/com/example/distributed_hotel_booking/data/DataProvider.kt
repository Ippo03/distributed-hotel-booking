package com.example.distributed_hotel_booking.data

object DataProvider {
    val roomsList = listOf(
        Room("1", "Hilton Athens", "Luxury hotel with Acropolis views", DateRange("20/05/2024", "25/06/2024"), 1),
        Room("2", "Grand Hyatt Athens", "Modern hotel near Plaka", DateRange("10/06/2024", "15/07/2024"), 3),
        Room("3", "InterContinental Athenaeum", "Elegant hotel with rooftop pool", DateRange("05/07/2024", "10/08/2024"), 4),
        Room("4", "Hotel Grande Bretagne", "Historic hotel with Michelin restaurants", DateRange("15/08/2024", "20/09/2024"), 1),
        Room("5", "Electra Palace Athens", "Chic hotel with rooftop restaurant", DateRange("01/09/2024", "05/10/2024"), 2),
        Room("6", "Athens Marriott Hotel", "Contemporary hotel with fitness center", DateRange("10/10/2024", "15/11/2024"), 4),
        Room("7", "Wyndham Grand Athens", "Sleek hotel with panoramic views", DateRange("20/11/2024", "25/12/2024"), 4),
        Room("8", "Divani Caravel Hotel", "Upscale hotel with multiple dining options", DateRange("05/12/2024", "10/01/2025"), 3),
        Room("9", "Radisson Blu Park Hotel Athens", "Tranquil hotel with lush gardens", DateRange("15/01/2025", "20/02/2025"), 3),
        Room("10", "Athenaeum Grand Hotel", "Modern hotel with rooftop terrace", DateRange("01/02/2025", "05/03/2025"), 2)
    )

    fun getRoomById(roomId: String?): Room? {
        return roomsList.firstOrNull { it.id == roomId }
    }

}

