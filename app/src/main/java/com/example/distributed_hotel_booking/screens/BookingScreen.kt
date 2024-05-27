package com.example.distributed_hotel_booking.screens

import android.icu.text.SimpleDateFormat
import com.example.distributed_hotel_booking.components.DateRangePicker
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.data.Booking
import com.example.distributed_hotel_booking.data.DataProvider
import com.example.distributed_hotel_booking.data.DateRange
import com.example.distributed_hotel_booking.data.Room
import com.example.distributed_hotel_booking.util.parseDate
import com.example.distributed_hotel_booking.viewmodel.BookingViewModel
import com.example.distributed_hotel_booking.viewmodel.SharedViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val viewModel: BookingViewModel = viewModel()

    Log.d("BookingScreen", "Selected room: ${sharedViewModel.selectedRoom}")

    // get the user id from the shared view model
    val userId = sharedViewModel.userId.value
    Log.d("BookingScreen", "User id: $userId")

//    // create booking object
//    viewModel.booking.userId = userId.toString()
//    viewModel.booking.room = selectedRoom
//    Log.d("BookingScreen", "Booking room with id ${viewModel.booking.room.roomId}")


//    viewModel.booking.userId = sharedViewModel.userId.toString()
//    viewModel.booking.room = sharedViewModel.selectedRoom

    val maxGuests = sharedViewModel.selectedRoom.noOfGuests
    val pricePerNight = sharedViewModel.selectedRoom.price
    val dateTime = LocalDateTime.now()

    // Create a mutable state for the TextField value
    val guestCount = remember { mutableStateOf("1") }
    val errorMessage = remember { mutableStateOf("") }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") // for date picker
    val log_formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // for log output only
    val focusRequester = remember { FocusRequester() }
    val selectedCheckInDateText = remember { mutableStateOf("") }
    val selectedCheckOutDateText = remember { mutableStateOf("") }
    val totalAmount = remember { mutableStateOf(Booking.calculateTotal(viewModel.booking))}

    Surface(color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Booking Screen",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                Row {
                    Text(
                        text = sharedViewModel.selectedRoom.roomName,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        text = "$pricePerNight €/night",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            item {
                Row {
                    OutlinedTextField(
                        value = guestCount.value, // Use the state as the TextField value
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { newValue: String ->
                            // Check if the entered number of guests exceeds the room's capacity
                            if (newValue.isNotEmpty() && newValue.toIntOrNull() != null && newValue.toInt() > (maxGuests ?: 0)) {
                                errorMessage.value = "The room's capacity is $maxGuests guests"
                            } else {
                                errorMessage.value = ""
                            }
                            guestCount.value = newValue
                        },
                        label = { Text(text = "Enter number of guests") },
                        modifier = Modifier
                            .padding(16.dp)
                            .wrapContentSize()
                            .onKeyEvent {
                                if (it.key == Key.Enter) {
                                    focusRequester.requestFocus(); true
                                } else false
                            },
                        isError = errorMessage.value.isNotEmpty(), // Show an error if the error message is not empty
                        supportingText = {Text(text = errorMessage.value) } // Show the error message
                    )
                }
            }
            item {
                Box(modifier = Modifier.aspectRatio(1.0f)) {
                    DateRangePicker(
                        dateTime = dateTime,
                        focusRequester = focusRequester,
                        clearFilters = null,
                        selectedStartDateText = selectedCheckInDateText,
                        selectedEndDateText = selectedCheckOutDateText,
                        onDateSelected = { checkIn, checkOut ->
                            selectedCheckInDateText.value = checkIn ?: selectedCheckInDateText.value
                            selectedCheckOutDateText.value = checkOut ?: selectedCheckOutDateText.value
                            viewModel.booking.dateRange = DateRange(parseDate(selectedCheckInDateText.value), parseDate(selectedCheckOutDateText.value))
                            Log.d("BOOKING SCREEN", "Selected dates: ${viewModel.booking.dateRange?.startDate} - ${viewModel.booking.dateRange?.endDate}")
                            Booking.calculateTotal(viewModel.booking)
                            totalAmount.value = viewModel.booking.total!!
                            Log.d("BOOKING SCREEN", "TOTAL: ${viewModel.booking.total}")
                            Log.d("BOOKING SCREEN", "AMOUNT: ${totalAmount.value}")
                        }
                    )
                }
            }
            item {
                Text(
                    text = "Total amount: " + totalAmount.value.toString() + " €",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp))
            }
            item{
                Row {
                    Button(
                        onClick = {
                            val booking = Booking(
                                userId,
                                sharedViewModel.selectedRoom,
                                viewModel.booking.dateRange,
                                guestCount.value.toInt(),
                                viewModel.booking.total
                            )
                            viewModel.booking = booking
                            Log.d("onBook", "Booking room with booking: $booking")
//                            Log.d("BookingScreen","Before navigation: roomId=${viewModel.booking.room?.roomId}, checkInDate=${log_formatter.format(viewModel.booking.dateRange?.startDate)}, checkOutDate=${log_formatter.format(viewModel.booking.dateRange?.endDate)}, guests=${viewModel.booking.guests}, total=${viewModel.booking.total}")
                            viewModel.onBook(navController, sharedViewModel, context)
                        },
                        content = { Text("Book Room") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
//TODO: NOT WORKING YET NEED TO CONFIGURE
fun showBookingResult(navController: NavController, success: Int, message: String) {
        if (success == 1) {
            // If the booking was successful, show a success message and navigate to the My Bookings screen
            Toast.makeText(
                navController.context,
                "Your booking was successful. Enjoy your stay !",
                Toast.LENGTH_LONG
            ).show()
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(Screen.HomeScreen.route) { inclusive = false }
            }
        } else {
            // If the booking was unsuccessful, show an error message and navigate to the RoomDetailsScreen
            Toast.makeText(
                navController.context,
                "Booking was unsuccessful. Please try again.",
                Toast.LENGTH_LONG
            ).show()
            // navController.navigate(Screen.BookingScreen.route) -> maybe no need to force recompose of BookingScreen
        }
}
