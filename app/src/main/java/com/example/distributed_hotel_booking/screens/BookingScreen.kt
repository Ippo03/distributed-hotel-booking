package com.example.distributed_hotel_booking.screens

import android.icu.text.SimpleDateFormat
import com.example.distributed_hotel_booking.components.DateRangePicker
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.data.Booking
import com.example.distributed_hotel_booking.data.DateRange
import com.example.distributed_hotel_booking.data.RoomInfo
import com.example.distributed_hotel_booking.util.parseDate
import com.example.distributed_hotel_booking.viewmodel.BookingViewModel
import com.example.distributed_hotel_booking.viewmodel.SharedViewModel
import java.time.LocalDateTime

@Composable
fun BookingScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val viewModel: BookingViewModel = viewModel()
    viewModel.booking.userData = sharedViewModel.userData
    viewModel.booking.roomInfo = RoomInfo(sharedViewModel.selectedRoom.roomId, sharedViewModel.selectedRoom.roomName, sharedViewModel.selectedRoom.roomImage)

    // Get the room's capacity and price
    val maxGuests = sharedViewModel.selectedRoom.noOfGuests
    val pricePerNight = sharedViewModel.selectedRoom.price
    val dateTime = LocalDateTime.now()

    // Create a mutable state for the TextField value
    val guestCount = remember { mutableStateOf("1") }
    val errorMessage = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val selectedCheckInDateText = remember { mutableStateOf("") }
    val selectedCheckOutDateText = remember { mutableStateOf("") }
    val totalAmount = remember { mutableStateOf(Booking.calculateTotal(viewModel.booking, sharedViewModel.selectedRoom.price))}

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
                        value = guestCount.value,
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
                        isError = errorMessage.value.isNotEmpty(),
                        supportingText = {Text(text = errorMessage.value) }
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
                            Booking.calculateTotal(viewModel.booking, sharedViewModel.selectedRoom.price)
                            totalAmount.value = viewModel.booking.total!!
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
                                viewModel.booking.userData,
                                viewModel.booking.roomInfo,
                                viewModel.booking.dateRange,
                                guestCount.value.toInt(),
                                viewModel.booking.total
                            )
                            viewModel.booking = booking
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
