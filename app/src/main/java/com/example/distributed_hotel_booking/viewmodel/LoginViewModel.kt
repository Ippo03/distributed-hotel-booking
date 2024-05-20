package com.example.distributed_hotel_booking.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.connector.BackendConnector
import com.example.distributed_hotel_booking.connector.TransmissionObjectType
import com.example.distributed_hotel_booking.screens.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    var usernameText = mutableStateOf(TextFieldValue(""))
    var passwordText = mutableStateOf(TextFieldValue(""))

    fun onLogin(navController: NavController, sharedViewModel: SharedViewModel) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val backendConnector = BackendConnector.getInstance()
            val transmissionObject = BackendConnector.createTransmissionObject(
                TransmissionObjectType.LOGIN,
            )
            transmissionObject.username = usernameText.value.text
            transmissionObject.password = passwordText.value.text

            Log.d("Username and password", "${transmissionObject.username} ${transmissionObject.password}")

            val response = backendConnector.sendRequest(transmissionObject)
            Log.d("Response", response.toString());

            withContext(Dispatchers.Main) {
                if (response == null) {
                    sharedViewModel.showSnackbar("Failed to connect to server")
                    return@withContext
               }

                if (response.success == 1) {
                    Log.d("Login", "Login successful")
                    sharedViewModel.updateUserData(response)
                    sharedViewModel.username.value = usernameText.value.text
                    sharedViewModel.showSnackbar("Login successful")
                    navController.navigate(Screen.HomeScreen.route)
                }
            }
        }
    }
}