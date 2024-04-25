package com.example.distributed_hotel_booking

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigator() {
    // This is the main navigation component of the app
    // It should handle the navigation between different screens
    // It should contain a NavHost that defines the navigation routes
    // It should contain a NavHostController that controls the navigation
    val navController = rememberNavController()
    // add a view model

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        // define the navigation routes here
         composable(Screen.LoginScreen.route) { LoginScreen() }
         composable(Screen.UserHomeScreen.route) { UserHomeScreen() }
         composable(Screen.BookingScreen.route) { BookingScreen() }
         composable(Screen.RoomDetailsScreen.route) { RoomDetailsScreen() }
    }

}