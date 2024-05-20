package com.example.distributed_hotel_booking.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.distributed_hotel_booking.connector.TransmissionObject
import com.example.distributed_hotel_booking.connector.user.UserData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    var userId = mutableStateOf(0)
    var username = mutableStateOf("")

    // late init
    var userData: UserData = UserData()


    private val snackbarChannel = Channel<String>(Channel.BUFFERED)
    val snackbarFlow = snackbarChannel.receiveAsFlow()

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            snackbarChannel.send(message)
        }
    }

    fun updateUserData(transmissionObject: TransmissionObject) {
        userId.value = transmissionObject.userData.userId
        this.userData = transmissionObject.userData
    }

    fun clearData() {
        userId = mutableStateOf(0)
        username = mutableStateOf("")
        userData = UserData()
    }

}