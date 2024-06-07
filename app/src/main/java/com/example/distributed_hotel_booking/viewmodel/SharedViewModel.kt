package com.example.distributed_hotel_booking.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.distributed_hotel_booking.connector.BackendConnector
import com.example.distributed_hotel_booking.connector.TransmissionObject
import com.example.distributed_hotel_booking.connector.TransmissionObjectBuilder
import com.example.distributed_hotel_booking.connector.TransmissionObjectType
import com.example.distributed_hotel_booking.data.UserData
import com.example.distributed_hotel_booking.data.Booking
import com.example.distributed_hotel_booking.data.DateRange
import com.example.distributed_hotel_booking.data.Room
import com.example.distributed_hotel_booking.data.RoomInfo
import com.example.distributed_hotel_booking.util.getRandomProfilePicture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Instant
import java.util.Date

class SharedViewModel : ViewModel() {

    var userData: UserData = UserData()

    var selectedBooking = Booking(userData, null, null, 0, 0f, null)
    fun updateSelectedBooking(booking: Booking) {
        selectedBooking = booking
    }

    var roomsList = mutableListOf<Room>()

    var userBookings = mutableStateListOf<Booking>()

    var selectedRoom = Room(
        "",
        "",
        DateRange(Date.from(Instant.now()), Date.from(Instant.now())),
        0,
        BigDecimal.ZERO,
        0f
    )

    private val snackbarChannel = Channel<String>(Channel.BUFFERED)
    val snackbarFlow = snackbarChannel.receiveAsFlow()

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            snackbarChannel.send(message)
        }
    }

    fun updateUserData(transmissionObject: TransmissionObject) {
        this.userData = transmissionObject.userData
    }

    fun clearData() {
        userData = UserData()
        selectedRoom = Room(
            "",
            "",
            DateRange(Date.from(Instant.now()), Date.from(Instant.now())),
            0,
            BigDecimal.ZERO,
            0f
        )
        roomsList.clear()
        userBookings.clear()
        selectedBooking = Booking(userData, null, null, 0, 0f, null)
    }

    fun updateBookings() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val transmissionObject = TransmissionObjectBuilder()
                .type(TransmissionObjectType.GET_USER_BOOKINGS)
                .userData(userData)
                .build()
            val backendConnector = BackendConnector.getInstance()
            val response = backendConnector.sendRequest(transmissionObject)
            if (response.userBookings != null) {
                this@SharedViewModel.userBookings.clear()
                this@SharedViewModel.userBookings.addAll(response.userBookings)
                Log.d("User Bookings", userBookings.toString())
            } else {
                Log.d("User Bookings", "No bookings found")
            }
        }
    }

}