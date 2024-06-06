package com.example.distributed_hotel_booking.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.connector.BackendConnector
import com.example.distributed_hotel_booking.connector.TransmissionObjectBuilder
import com.example.distributed_hotel_booking.connector.TransmissionObjectType
import com.example.distributed_hotel_booking.data.DateRange
import com.example.distributed_hotel_booking.data.Review
import com.example.distributed_hotel_booking.data.Room
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

                delay(1000)

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
                    // "ReCompose" the HomeScreen
                    navContoller.navigate(Screen.HomeScreen.route)
                }
            }
        }
    }

}