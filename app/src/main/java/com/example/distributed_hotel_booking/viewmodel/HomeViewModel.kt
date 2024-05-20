package com.example.distributed_hotel_booking.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.connector.BackendConnector
import com.example.distributed_hotel_booking.connector.TransmissionObjectBuilder
import com.example.distributed_hotel_booking.connector.TransmissionObjectType
import com.example.distributed_hotel_booking.data.DataProvider
import com.example.distributed_hotel_booking.data.DateRange
import com.example.distributed_hotel_booking.data.SearchFilter
import com.example.distributed_hotel_booking.screens.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    var searchFilter = SearchFilter("", DateRange("",""), "", 0, 0f, 0)
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

            if (response.success == 1) {
                withContext(Dispatchers.Main) {
                    navContoller.navigate(Screen.RoomDetailsScreen.route)
                }
            }
        }
    }
}