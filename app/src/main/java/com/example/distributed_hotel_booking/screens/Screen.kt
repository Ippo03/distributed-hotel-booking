package com.example.distributed_hotel_booking.screens

sealed class Screen(val route: String) {
    // define the screens in the app like this
    data object SplashScreen : Screen("splash_screen")
    data object LoginScreen : Screen("login_screen")
    data object UserHomeScreen : Screen("user_home_screen")
    data object BookingScreen : Screen("booking_screen")
    data object RoomDetailsScreen : Screen("room_details_screen")
    data object PaymentScreen : Screen("payment_screen")
}

