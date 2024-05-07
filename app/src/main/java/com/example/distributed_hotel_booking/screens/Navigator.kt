package com.example.distributed_hotel_booking.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            "${Screen.RoomDetailsScreen.route}/{roomId}", // Define the argument in the route
            arguments = listOf(navArgument("roomId") {
                type = NavType.StringType
            }) // Define the argument
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") // Retrieve the argument
            RoomDetailsScreen(navController, roomId)
        }
        composable(
            "${Screen.BookingScreen.route}/{roomId}", // Define the argument in the route
            arguments = listOf(navArgument("roomId") {
                type = NavType.StringType
            }) // Define the argument
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") // Retrieve the argument
            BookingScreen(navController, roomId)
        }
        composable(
            route = "${Screen.PaymentScreen.route}/{roomId}/{checkInDate}/{checkOutDate}/{guestCount}",
            arguments = listOf(
                navArgument("roomId") { type = NavType.StringType },
                navArgument("checkInDate") { type = NavType.StringType },
                navArgument("checkOutDate") { type = NavType.StringType },
                navArgument("guestCount") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId")
            val checkInDate = backStackEntry.arguments?.getString("checkInDate")
            val checkOutDate = backStackEntry.arguments?.getString("checkOutDate")
            val guests = backStackEntry.arguments?.getString("guestCount")
            PaymentScreen(navController, roomId, checkInDate, checkOutDate, guests)
        }
    }

}