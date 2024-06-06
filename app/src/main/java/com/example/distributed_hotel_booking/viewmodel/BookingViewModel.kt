package com.example.distributed_hotel_booking.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.connector.BackendConnector
import com.example.distributed_hotel_booking.connector.TransmissionObjectBuilder
import com.example.distributed_hotel_booking.connector.TransmissionObjectType
import com.example.distributed_hotel_booking.data.Booking
import com.example.distributed_hotel_booking.screens.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookingViewModel : ViewModel() {
    var booking = Booking(0, null, null, null,0f) // TODO: REMOVE ROOM FROM BOOKING OBJECT OR CHANGE TO NULL OR ONLY ROOMINFO OBJECT OR ONLY ROOMNAME

    fun onBook(navController: NavController, sharedViewModel: SharedViewModel, context: Context) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val transmissionObject = TransmissionObjectBuilder()
                .type(TransmissionObjectType.BOOK)
                .booking(booking)
                .build()

            Log.d("Book", "Booking room with booking: $booking")
            val backendConnector = BackendConnector.getInstance()
            val response = backendConnector.sendRequest(transmissionObject)
            Log.d("Response", response.toString())

            withContext(Dispatchers.Main) {
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                if (response.success == 1) {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
                        sharedViewModel.roomsList.clear() // Clear the list of rooms
                    }
                } else {
                    navController.navigate(Screen.BookingScreen.route) {
                        popUpTo(Screen.BookingScreen.route) { inclusive = true }
                    }
                }
            }
        }

    }
}