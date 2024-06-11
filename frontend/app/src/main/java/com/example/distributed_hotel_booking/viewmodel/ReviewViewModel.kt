package com.example.distributed_hotel_booking.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.connector.BackendConnector
import com.example.distributed_hotel_booking.connector.TransmissionObjectBuilder
import com.example.distributed_hotel_booking.connector.TransmissionObjectType
import com.example.distributed_hotel_booking.data.Review
import com.example.distributed_hotel_booking.data.RoomInfo
import com.example.distributed_hotel_booking.screens.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewViewModel : ViewModel() {
    fun onReview(navController: NavController, sharedViewModel: SharedViewModel,  context: Context, currentReview: Review) {
        // Check if the rating is between 1 and 5 (inclusive)
        if (currentReview.rating != null && currentReview.rating in 1..5) {
            currentReview.roomInfo = sharedViewModel.selectedBooking.roomInfo!!

            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                val transmissionObject = TransmissionObjectBuilder()
                    .type(TransmissionObjectType.REVIEW)
                    .review(currentReview)
                    .bookingDates(sharedViewModel.selectedBooking.dateRange)
                    .message("NEW")
                    .build()

                val backendConnector = BackendConnector.getInstance()
                val response = backendConnector.sendRequest(transmissionObject)

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    if (response.success == 1) {
                        navController.navigate(Screen.UserBookingsScreen.route) {
                            popUpTo(Screen.UserBookingsScreen.route) { inclusive = true }
                        }
                    }
                }
            }
        } else {
            // If the rating is not between 1 and 5 (inclusive), display a Toast message
            Toast.makeText(context, "Please provide a rating !", Toast.LENGTH_LONG).show()
        }
    }

    fun onUpdate(navController: NavController, sharedViewModel: SharedViewModel,  context: Context, currentReview: Review) {
        if (currentReview.rating != null && currentReview.rating in 1..5) {
            currentReview.roomInfo = RoomInfo(
                sharedViewModel.selectedBooking.roomInfo!!.roomId,
                sharedViewModel.selectedBooking.roomInfo!!.roomName
            )

            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                val transmissionObject = TransmissionObjectBuilder()
                    .type(TransmissionObjectType.REVIEW)
                    .review(currentReview)
                    .bookingDates(sharedViewModel.selectedBooking.dateRange)
                    .message("UPDATE")
                    .build()

                val backendConnector = BackendConnector.getInstance()
                val response = backendConnector.sendRequest(transmissionObject)

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    if (response.success == 1) {
                        navController.navigate(Screen.UserBookingsScreen.route) {
                            popUpTo(Screen.UserBookingsScreen.route) { inclusive = true }
                        }
                    }
                }
            }
        } else {
            // If the rating is not between 1 and 5 (inclusive), display a Toast message
            Toast.makeText(context, "Please provide a rating !", Toast.LENGTH_LONG).show()
        }
    }

    fun onDelete(navController: NavController, sharedViewModel: SharedViewModel,  context: Context, currentReview: Review) {
        currentReview.roomInfo = RoomInfo(sharedViewModel.selectedBooking.roomInfo!!.roomId, sharedViewModel.selectedBooking.roomInfo!!.roomName)

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val transmissionObject = TransmissionObjectBuilder()
                .type(TransmissionObjectType.REVIEW)
                .review(currentReview)
                .bookingDates(sharedViewModel.selectedBooking.dateRange)
                .message("DELETE")
                .build()

            val backendConnector = BackendConnector.getInstance()
            val response = backendConnector.sendRequest(transmissionObject)

            withContext(Dispatchers.Main) {
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                if (response.success == 1) {
                    navController.navigate(Screen.UserBookingsScreen.route) {
                        popUpTo(Screen.UserBookingsScreen.route) { inclusive = true }
                    }
                }
            }
        }
    }
}