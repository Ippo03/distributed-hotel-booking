package com.example.distributed_hotel_booking

sealed class Screen(val route: String) {
    // define the screens in the app like this
    data object LoginScreen : Screen("login_screen")
    data object HomeScreen : Screen("home_screen")
    data object BookingScreen : Screen("booking_screen")
    data object RoomDetailsScreen : Screen("rooms_details_screen")
    data object UserHomeScreen : Screen("user_home_screen")
}
