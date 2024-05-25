package com.example.distributed_hotel_booking.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.connector.BackendConnector
import com.example.distributed_hotel_booking.connector.TransmissionObjectBuilder
import com.example.distributed_hotel_booking.connector.TransmissionObjectType
import com.example.distributed_hotel_booking.data.DataProvider
import com.example.distributed_hotel_booking.data.DateRange
import com.example.distributed_hotel_booking.data.Review
import com.example.distributed_hotel_booking.data.SearchFilter
import com.example.distributed_hotel_booking.screens.Screen
import com.example.distributed_hotel_booking.util.getMaxDate
import com.example.distributed_hotel_booking.util.getToday
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class HomeViewModel : ViewModel() {
    var searchFilter = SearchFilter("", DateRange(getToday(), getMaxDate()), "", 0, BigDecimal.ZERO, 0)
    var review = Review("", DataProvider.roomsList[0], 0, "");
    //var searchFilter : SearchFilter = SearchFilter()
    // Called at the start of the HomeScreen
    fun updateRoomsList(navController: NavController, sharedViewModel: SharedViewModel) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val backendConnector = BackendConnector.getInstance()
            val transmissionObject = BackendConnector.createTransmissionObject(
                TransmissionObjectType.GET_ALL_ROOMS,
            )

            val response = backendConnector.sendRequest(transmissionObject)
            Log.d("Response", response.toString());

            withContext(Dispatchers.Main) {
                if (response == null) {
                    sharedViewModel.showSnackbar("Failed to connect to server")
                    return@withContext
                }

                if (response.success == 1) {
                    Log.d("Retrieve rooms", "Rooms retrieved successfully")
                    // Update the DataProvider/roomsList
                    //sharedViewModel.roomsList = response.rooms;
                    Log.d("Rooms", sharedViewModel.roomsList.toString())
                }
            }
        }
    }

    fun onLogout(navController: NavController, sharedViewModel: SharedViewModel) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val transmissionObject = TransmissionObjectBuilder()
                .type(TransmissionObjectType.LOGOUT)
                .build()

            val backendConnector = BackendConnector.getInstance()
            val response = backendConnector.sendRequest(transmissionObject)

            if (response.success == 1) {
                withContext(Dispatchers.Main) {
                    sharedViewModel.showSnackbar("Logout successful")
                    navController.navigate(Screen.LoginScreen.route)
                }

                delay(5000)

                sharedViewModel.clearData()
            }
        }
    }
    fun onSearch(navContoller: NavController, sharedViewModel: SharedViewModel) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val transmissionObject = TransmissionObjectBuilder()
                .type(TransmissionObjectType.SEARCH)
                .searchFilter(searchFilter)
                .build()
            Log.d("Search", "Searching for rooms with filter: $searchFilter")
            val backendConnector = BackendConnector.getInstance()
            val response = backendConnector.sendRequest(transmissionObject)
            Log.d("Response", response.toString())
            for (room in response.rooms) {
                Log.d("Room", room.toString())
            }
            sharedViewModel.roomsList = response.rooms

            if (response.success == 1) {
                withContext(Dispatchers.Main) {
                    navContoller.navigate(Screen.HomeScreen.route)
                }
            }
        }
    }

    fun onReview(navController: NavController, homeViewModel: HomeViewModel) {
        Log.d("Review", "Review button clicked")
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val transmissionObject = TransmissionObjectBuilder()
                .type(TransmissionObjectType.REVIEW)
                .review(review)
                .build()

            Log.d("Review", "Reviewing room with review: $review");
            val backendConnector = BackendConnector.getInstance()
            val response = backendConnector.sendRequest(transmissionObject)

            if (response.success == 1) {
                // Show Snackbar with the message from the response
                withContext(Dispatchers.Main) {
                    val message = response.message // Assuming message is the field containing the response message
                    Log.d("Review", "Review successful: $message")
                    // Navigate to HomeScreen
                    navController.navigate(Screen.HomeScreen.route)
                }
            }
        }
    }
}