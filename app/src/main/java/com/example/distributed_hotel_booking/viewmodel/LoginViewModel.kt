package com.example.distributed_hotel_booking.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
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

    var usernameError = mutableStateOf("")
    var passwordError = mutableStateOf("")

    fun onLogin(navController: NavController, sharedViewModel: SharedViewModel, context: Context) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val backendConnector = BackendConnector.getInstance()
            val transmissionObject = BackendConnector.createTransmissionObject(
                TransmissionObjectType.LOGIN,
            )
            transmissionObject.username = usernameText.value.text.trim()
            transmissionObject.password = passwordText.value.text.trim()

            Log.d("Username and password", "${transmissionObject.username} ${transmissionObject.password}")

            val response = backendConnector.sendRequest(transmissionObject)
            Log.d("Decoded Response", response.toString());

            withContext(Dispatchers.Main) {
                if (response == null) {
                    sharedViewModel.showSnackbar("Failed to connect to server")
                    return@withContext
               }

                if (response.success == 1) {
                    Log.d("Login", "Login successful")
                    sharedViewModel.updateUserData(response)
                    Toast.makeText(context, "Login was successful!", Toast.LENGTH_SHORT).show()
                    // Navigate to home screen
                    navController.navigate(Screen.HomeScreen.route)
                    sharedViewModel.roomsList.clear() // Clear the list of rooms
                } else if (response.success == 0) {
                    Log.d("Login", "Login failed")
                    // Display error messages
                    usernameError.value = "Invalid username"
                    passwordError.value = "Invalid password"
                    // Clear the text fields
                    usernameText.value = TextFieldValue("")
                    passwordText.value = TextFieldValue("")
                    Toast.makeText(context, "Login failed! Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}