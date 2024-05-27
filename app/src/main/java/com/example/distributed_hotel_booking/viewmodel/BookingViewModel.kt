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
import com.example.distributed_hotel_booking.screens.showBookingResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookingViewModel : ViewModel() {
    var booking = Booking(0, null, null, null,0f)

//   fun onBook(navController: NavController, sharedViewModel: SharedViewModel){
//       var success: Int = -1
//       var message: String = ""
//        // Send booking to backend
//        val scope = CoroutineScope(Dispatchers.IO)
//        scope.launch {
//            val transmissionObject = TransmissionObjectBuilder()
//                .type(TransmissionObjectType.BOOK)
//                .booking(booking)
//                .build()
//
//            Log.d("SENT BOOKING", transmissionObject.toString())
//            Log.d("Booking", "User with id ${booking.userId} is booking the: ${booking.room?.roomName} for ${booking.guests} guests from ${booking.dateRange?.startDate} to ${booking.dateRange?.endDate} and is going to pay ${booking.total} euros")
//            val backendConnector = BackendConnector.getInstance()
//            val response = backendConnector.sendRequest(transmissionObject)
//            Log.d("Response", response.toString())
//            success = response.success
//            message = response.message
////            if (response.success == 1) {
////                withContext(Dispatchers.Main) {
////                    sharedViewModel.showSnackbar(response.message)
////                }
////            }
////            else {
////                withContext(Dispatchers.Main) {
////                    sharedViewModel.showSnackbar("Booking failed")
////                    // TODO : ADD MORE DESCRIPTIVE ERROR MESSAGE
////                    /*
////                    THROW POP UP ERROR MESSAGE
////                    CHANGE SOMETHING TO THE UI
////                    INFORM USER ABOUT THAT ROOMS AVAILABLE DATES
////                     */
////                }
////            }
//        }
//       Log.d("SUCCESS-VALUE", success.toString())
//       Log.d("MESSAGE-VALUE", message)
//       // SHOWS POP UP MESSAGE AND NAVIGATES TO THE HOME SCREEN IF BOOKING WAS SUCCESSFUL OR TO THE BOOKING SCREEN IF NOT
//       showBookingResult(navController, success, message)
//    }

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

//            if (response.success == 1) {
//                withContext(Dispatchers.Main) {
//                    sharedViewModel.showSnackbar(response.message)
//                    navController.navigate(Screen.HomeScreen.route) {
//                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
//                    }
//                }
//            } else {
//                withContext(Dispatchers.Main) {
//                    sharedViewModel.showSnackbar(response.message)
//                    navController.navigate(Screen.BookingScreen.route) {
//                        popUpTo(Screen.BookingScreen.route) { inclusive = true }
//                    }
//                }
//            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                if (response.success == 1) {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
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