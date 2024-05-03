package com.example.distributed_hotel_booking

import RoomDetailsScreen
import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.distributed_hotel_booking.data.Room
import com.example.distributed_hotel_booking.viewmodel.SharedViewModel

@Composable
fun Navigator() {
    // This is the main navigation component of the app
    // It should handle the navigation between different screens
    // It should contain a NavHost that defines the navigation routes
    // It should contain a NavHostController that controls the navigation
    val navController = rememberNavController()
    val sharedViewModel = SharedViewModel()

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        // define the navigation routes here
         composable(Screen.SplashScreen.route) { SplashScreen(navController) }
         composable(Screen.LoginScreen.route) { LoginScreen(navController, sharedViewModel) }
         composable(Screen.UserHomeScreen.route) { UserHomeScreen(navController) }
         composable(Screen.BookingScreen.route) { BookingScreen() }
         composable(Screen.RoomDetailsScreen.route) { RoomDetailsScreen(navController) }
    }

}