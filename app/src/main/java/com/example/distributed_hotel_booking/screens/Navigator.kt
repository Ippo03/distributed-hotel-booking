package com.example.distributed_hotel_booking.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.distributed_hotel_booking.data.Booking
import com.example.distributed_hotel_booking.viewmodel.SharedViewModel

@Composable
fun Navigator(sharedViewModel: SharedViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SplashScreen.route) { SplashScreen(navController) }
        composable(Screen.LoginScreen.route) { LoginScreen(navController, sharedViewModel) }
        composable(Screen.HomeScreen.route) { HomeScreen(navController, sharedViewModel) }
        composable(Screen.UserBookingsScreen.route) { UserBookingsScreen(navController, sharedViewModel) }
        composable(Screen.RoomDetailsScreen.route) {RoomDetailsScreen(navController, sharedViewModel)}
        composable(Screen.BookingScreen.route) { BookingScreen(navController, sharedViewModel) }
    }
}