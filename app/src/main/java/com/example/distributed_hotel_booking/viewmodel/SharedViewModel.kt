package com.example.distributed_hotel_booking.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.connector.BackendConnector
import com.example.distributed_hotel_booking.connector.TransmissionObject
import com.example.distributed_hotel_booking.connector.TransmissionObjectBuilder
import com.example.distributed_hotel_booking.connector.TransmissionObjectType
import com.example.distributed_hotel_booking.connector.user.UserData
import com.example.distributed_hotel_booking.data.Booking
import com.example.distributed_hotel_booking.data.DateRange
import com.example.distributed_hotel_booking.data.Review
import com.example.distributed_hotel_booking.data.Room
import com.example.distributed_hotel_booking.data.RoomInfo
import com.example.distributed_hotel_booking.util.getRandomProfilePicture
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
    var userProfilePicture = mutableStateOf("")

    // late init
    var userData: UserData = UserData()

    var roomInfo = RoomInfo() //Note: THOUGHT: DON'T HAVE A ROOM OBJECT IN THE BOOKING AND/ OR THE REVIEW OBJECTS.

    var selectedBooking = Booking(-1, null, null, 0, 0f, null)
    fun updateSelectedBooking(booking: Booking) {
        selectedBooking = booking
        selectedBooking.room?.bookings = emptyList() // TODO : NOT GOOD SOLUTION
    }

    var roomsList = mutableListOf<Room>()

    var userBookings = mutableStateListOf<Booking>()
    //var userReviews = mutableStateListOf<Review>()

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
        userId.value = transmissionObject.userData.id
        userProfilePicture.value = getRandomProfilePicture()
        this.userData = transmissionObject.userData
    }

    fun clearData() {
        userId = mutableStateOf(0)
        username = mutableStateOf("")
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
        selectedBooking = Booking(-1, null, null, 0, 0f, null)
    }

    fun updateBookings() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val transmissionObject = TransmissionObjectBuilder()
                .type(TransmissionObjectType.GET_USER_BOOKINGS)
                .userData(userData) // what else could we send ???
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


    // TODO: TO DELETE --> THE ABOVE METHOD ALREADY DOES (OR SHOULD DO) WHAT WE NEED
//    fun updateReviews() {
//        val scope = CoroutineScope(Dispatchers.IO)
//        scope.launch {
//            val transmissionObject = TransmissionObjectBuilder()
//                .type(TransmissionObjectType.GET_USER_REVIEWS)
//                .userData(userData) // what else could we send ???
//                .build()
//            val backendConnector = BackendConnector.getInstance()
//            val response = backendConnector.sendRequest(transmissionObject)
//            if (response.userReviews != null) {
//                this@SharedViewModel.userReviews.clear()
//                this@SharedViewModel.userReviews.addAll(response.userReviews)
//                Log.d("User Reviews", userReviews.toString())
//            } else {
//                Log.d("User Reviews", "No reviews found")
//            }
//        }
//    }
}