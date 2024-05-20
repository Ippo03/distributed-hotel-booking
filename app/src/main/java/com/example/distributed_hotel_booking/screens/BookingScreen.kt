package com.example.distributed_hotel_booking.screens

import com.example.distributed_hotel_booking.components.DateRangePicker
import android.util.Log
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.data.DataProvider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(navController: NavController, roomId: String?) {

    Log.d("BookingScreen", "Booking room with id $roomId")
    val room = DataProvider.getRoomById(roomId)
    val maxGuests = room?.guests
    val pricePerNight = room?.price
    val dateTime = LocalDateTime.now()
    // Create a mutable state for the TextField value
    val guestCount = remember { mutableStateOf("1") }
    val errorMessage = remember { mutableStateOf("") }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val focusRequester = remember { FocusRequester() }
    var selectedCheckInDateText = remember { mutableStateOf("") }
    var selectedCheckOutDateText = remember { mutableStateOf("") }

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
                        text = room?.name ?: "Room not found",
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
//                    TextField(
//                        value = guestCount.value, // Use the state as the TextField value
//                        singleLine = true,
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        onValueChange = { newValue: String ->
//                            // Check if the entered number of guests exceeds the room's capacity
//                            if (newValue.toIntOrNull() != null && newValue.toInt() > (maxGuests
//                                    ?: 0)
//                            ) {
//                                errorMessage.value = "The room's capacity is $maxGuests guests"
//                            } else {
//                                errorMessage.value = ""
//                            }
//                            guestCount.value = newValue
//                        },
//                        label = { Text(text = "Enter number of guests") },
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .wrapContentSize()
//                            .onKeyEvent {
//                                if (it.key == Key.Enter) {
//                                    focusRequester.requestFocus(); true
//                                } else false
//                            },
//                        isError = errorMessage.value.isNotEmpty(), // Show an error if the error message is not empty
//                        supportingText = { if (errorMessage.value.isNotEmpty()) Text(text = errorMessage.value) } // Show the error message
//                    )
                    OutlinedTextField(
                        value = guestCount.value, // Use the state as the TextField value
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { newValue: String ->
                            // Check if the entered number of guests exceeds the room's capacity
                            if (newValue.toIntOrNull() != null && newValue.toInt() > (maxGuests ?: 0)) {
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
                            selectedCheckInDateText = (checkIn ?: selectedCheckInDateText) as MutableState<String>
                            selectedCheckOutDateText = (checkOut ?: selectedCheckOutDateText) as MutableState<String>
                        }
                    )
                }
            }
            item{
                Row {
                    Button(
                        onClick = {
                            Log.d("BookingScreen","Before navigation: roomId=$roomId, checkInDate=${selectedCheckInDateText.value}, checkOutDate=$selectedCheckOutDateText, guests=${guestCount.value}")
                            navController.navigate("payment_screen/${roomId}/${selectedCheckInDateText.value}/${selectedCheckOutDateText.value}/${guestCount.value}") // Go to Payment/Booking Overview screen
                            Log.d("BookingScreen", "After navigation command")
                        },
                        content = { Text("Book Room") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
            Log.d(
                "BookingScreen",
                "Booking room ${room?.name} with ${guestCount.value} guests from ${selectedCheckInDateText.value} to ${selectedCheckOutDateText}"
            )
        }
    }
}
