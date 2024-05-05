package com.example.distributed_hotel_booking.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.distributed_hotel_booking.screens.BookingScreen
import com.example.distributed_hotel_booking.screens.LoginScreen
import com.example.distributed_hotel_booking.screens.Screen
import com.example.distributed_hotel_booking.screens.SplashScreen
import com.example.distributed_hotel_booking.screens.UserHomeScreen
import com.example.distributed_hotel_booking.viewmodel.SharedViewModel

@Composable
fun Navigator() {
    // This is the main navigation component of the app
    // It should handle the navigation between different screens
    // It should contain a NavHost that defines the navigation routes
    // It should contain a NavHostController that controls the navigation
    val navController = rememberNavController()
    val sharedViewModel = SharedViewModel()

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route, ) {
        // define the navigation routes here
         composable(Screen.SplashScreen.route) { SplashScreen(navController) }
         composable(Screen.LoginScreen.route) { LoginScreen(navController, sharedViewModel) }
         composable(Screen.UserHomeScreen.route) { UserHomeScreen(navController) }
        composable(
            "${Screen.BookingScreen.route}/{roomId}", // Define the argument in the route
            arguments = listOf(navArgument("roomId") { type = NavType.StringType }) // Define the argument
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") // Retrieve the argument
            BookingScreen(navController, roomId)
        }
        composable(
            "${Screen.RoomDetailsScreen.route}/{roomId}", // Define the argument in the route
            arguments = listOf(navArgument("roomId") { type = NavType.StringType }) // Define the argument
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") // Retrieve the argument
            RoomDetailsScreen(navController, roomId)
        }
    }

}