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
    // VIEWMODEL FOR THE USER BOOKINGS SCREEN FROM WHERE THE USER CAN VIEW HIS/HER REVIEWS AND ALSO LEAVE A REVIEW IF THE BOOKING DATES HAVE PASSED
    fun onReview(navController: NavController, sharedViewModel: SharedViewModel,  context: Context, currentReview: Review) {
        Log.d("Review", "Review button clicked")
        // Check if the rating is between 1 and 5 (inclusive)
        if (currentReview.rating != null && currentReview.rating in 1..5) {
            Log.d("REVIEW FROM SHARED VIEW MODEL", " NAME -> ${sharedViewModel.selectedBooking.roomInfo?.roomName} and ID ->  ${sharedViewModel.selectedBooking.roomInfo?.roomId}")
            currentReview.roomInfo = sharedViewModel.selectedBooking.roomInfo!!
            Log.d("REVIEW TO SEND", " NAME -> ${currentReview.roomInfo?.roomName} and ID ->  ${currentReview.roomInfo?.roomId}")
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                val transmissionObject = TransmissionObjectBuilder()
                    .type(TransmissionObjectType.REVIEW)
                    .review(currentReview)
                    .bookingDates(sharedViewModel.selectedBooking.dateRange)
                    .message("NEW")
                    .build()
                Log.d("Review", "Sending review to backend: $currentReview")
                val backendConnector = BackendConnector.getInstance()
                val response = backendConnector.sendRequest(transmissionObject)
                Log.d("ON Review", "Reviewed ${sharedViewModel.selectedBooking.roomInfo?.roomName} with review: $currentReview")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    if (response.success == 1) {
                        Log.d("ON Review", "Review successful: ${response.message}")
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
        Log.d("Review", "Update button clicked")
        if (currentReview.rating != null && currentReview.rating in 1..5) {
            Log.d(
                "REVIEW FROM SHARED VIEW MODEL",
                " NAME -> ${sharedViewModel.selectedBooking.roomInfo?.roomName} and ID ->  ${sharedViewModel.selectedBooking.roomInfo?.roomId}"
            )
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
                Log.d("Review", "Sending updated review to backend: $currentReview")
                val backendConnector = BackendConnector.getInstance()
                val response = backendConnector.sendRequest(transmissionObject)
                Log.d(
                    "ON Update Review",
                    "Updated review of ${sharedViewModel.selectedBooking.roomInfo?.roomName} with review: $currentReview"
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    if (response.success == 1) {
                        //sharedViewModel.updateReviews(); // Update the reviews list
                        Log.d("ON Update Review", "Review updated successfully: ${response.message}")
                        // TODO: See if re-composition through navigation is necessary for the reviews to be displayed
                        navController.navigate(Screen.UserBookingsScreen.route) {
                            popUpTo(Screen.UserBookingsScreen.route) { inclusive = true }
                            // Will force Screen to recompose and call the Update Booking method again which will update the bookings with their reviews.
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
        Log.d("Review", "Delete button clicked")
        Log.d("REVIEW FROM SHARED VIEW MODEL", " NAME -> ${sharedViewModel.selectedBooking.roomInfo?.roomName} and ID ->  ${sharedViewModel.selectedBooking.roomInfo?.roomId}")
        currentReview.roomInfo = RoomInfo(sharedViewModel.selectedBooking.roomInfo!!.roomId, sharedViewModel.selectedBooking.roomInfo!!.roomName)
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val transmissionObject = TransmissionObjectBuilder()
                .type(TransmissionObjectType.REVIEW)
                .review(currentReview)
                .bookingDates(sharedViewModel.selectedBooking.dateRange)
                .message("DELETE")
                .build()
            Log.d("Review", "Sending invalid review to backend: $currentReview")
            val backendConnector = BackendConnector.getInstance()
            val response = backendConnector.sendRequest(transmissionObject)
            Log.d("ON Delete Review", "Delete review of ${sharedViewModel.selectedBooking.roomInfo?.roomName} and is now null")
            withContext(Dispatchers.Main) {
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                if (response.success == 1) {
                    //sharedViewModel.updateReviews(); // Update the reviews list
                    Log.d("ON Delete Review", "Deletion of review successful: ${response.message}")
                    // TODO: See if re-composition through navigation is necessary for the reviews to be displayed
                    navController.navigate(Screen.UserBookingsScreen.route) {
                        popUpTo(Screen.UserBookingsScreen.route) { inclusive = true }
                    // Will force Screen to recompose and call the Update Booking method again which will update the bookings with their reviews.
                    }
                }
            }
        }
    }
}