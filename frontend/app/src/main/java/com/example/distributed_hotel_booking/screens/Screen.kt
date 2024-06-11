package com.example.distributed_hotel_booking.screens

sealed class Screen(val route: String) {
    data object SplashScreen : Screen("splash_screen")
    data object LoginScreen : Screen("login_screen")
    data object HomeScreen : Screen("home_screen")
    data object UserBookingsScreen : Screen("user_bookings_screen")
    data object UserReviewsScreen : Screen("user_reviews_screen")
    data object BookingScreen : Screen("booking_screen")
    data object RoomDetailsScreen : Screen("room_details_screen")
}

