package com.example.distributed_hotel_booking.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.connector.BackendConnector
import com.example.distributed_hotel_booking.connector.TransmissionObject
import com.example.distributed_hotel_booking.connector.TransmissionObjectType
import com.example.distributed_hotel_booking.connector.user.UserData
import com.example.distributed_hotel_booking.data.Booking
import com.example.distributed_hotel_booking.data.DateRange
import com.example.distributed_hotel_booking.data.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.Instant
import java.util.Date

class SharedViewModel : ViewModel() {
    var userId = mutableStateOf(0)
    var username = mutableStateOf("")

    // late init
    var userData: UserData = UserData()


    var roomsList = mutableListOf<Room>()
    // needs to be updated every time a room is
    // added or deleted & new bookings + reviews made
    // Maybe done in the background (in the DateProvider ?)

    var selectedRoom = Room("", "", DateRange(Date.from(Instant.now()), Date.from(Instant.now())),0, BigDecimal.ZERO)

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
        selectedRoom = Room("", "", DateRange(Date.from(Instant.now()), Date.from(Instant.now())),0, BigDecimal.ZERO)
    }

}