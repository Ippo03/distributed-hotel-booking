package com.example.distributed_hotel_booking.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.Screen

class LoginViewModel : ViewModel() {
    var usernameText = mutableStateOf(TextFieldValue(""))
    var passwordText = mutableStateOf(TextFieldValue(""))

    fun onLogin(navController: NavController, sharedViewModel: SharedViewModel) {
        // apply some backend logic here

        navController.navigate(Screen.UserHomeScreen.route)
    }
}