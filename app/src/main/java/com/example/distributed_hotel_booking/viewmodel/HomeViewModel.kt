package com.example.distributed_hotel_booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.connector.BackendConnector
import com.example.distributed_hotel_booking.connector.TransmissionObjectBuilder
import com.example.distributed_hotel_booking.connector.TransmissionObjectType
import com.example.distributed_hotel_booking.screens.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
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
}