package com.example.distributed_hotel_booking.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clipScrollableContainer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.magnifier
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.data.DataProvider
import com.example.distributed_hotel_booking.ui.theme.Distributed_hotel_bookingTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
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
    var checkInDate: String? = null
    var checkOutDate: String? = null
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Booking Screen",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
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
            Row {
                TextField(
                    value = guestCount.value, // Use the state as the TextField value
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
                        .wrapContentSize(),
                    isError = errorMessage.value.isNotEmpty(), // Show an error if the error message is not empty
                    supportingText = { if(errorMessage.value.isNotEmpty()) Text(text = errorMessage.value)} // Show the error message
                )
            }
            Row {
                // Saw from this YT Video: https://youtu.be/uggF_O4xe4I
                val dateRangePickerState = remember {
                    DateRangePickerState(
                        initialSelectedStartDateMillis = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
                        initialDisplayedMonthMillis = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
                        initialSelectedEndDateMillis = dateTime.plusDays(3).toInstant(ZoneOffset.UTC).toEpochMilli(),
                        initialDisplayMode = DisplayMode.Picker,
                        yearRange = (2024 ..2025))
                }
                DateRangePicker(state = dateRangePickerState,
                    showModeToggle = false, // If true, it will show the toggle button to switch between picker and calendar
                    title = {
                        val startDate = dateRangePickerState.selectedStartDateMillis?.let {
                            Instant.ofEpochMilli(
                                it
                            ).atZone(ZoneId.systemDefault()).toLocalDate()
                        }
                        val endDate = dateRangePickerState.selectedEndDateMillis?.let {
                            Instant.ofEpochMilli(
                                it
                            ).atZone(ZoneId.systemDefault()).toLocalDate()
                        }
                        Log.d("BookingScreen", "Selected dates: $startDate - $endDate")
                        if (endDate != null && startDate != null) {
                            checkInDate = startDate.format(formatter)
                            checkOutDate = endDate.format(formatter)
                            Text("Selected dates: $checkInDate - $checkOutDate")
                        }
                    },
                    //headline = { Text("Select the check-in and check-out dates", Modifier.size(150.dp)) },
                    dateValidator = { date ->
                        val localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
                        localDate.isAfter(dateTime.toLocalDate().minusDays(1))
                    },
                    modifier = Modifier
                        .animateContentSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .border(4.dp, MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .size(width = 350.dp, height = 400.dp),
                )
            }
            Row {
                Button(
                    onClick = {
                        Log.d("BookingScreen", "Before navigation: roomId=$roomId, checkInDate=$checkInDate, checkOutDate=$checkOutDate, guests=${guestCount.value}")
                        navController.navigate("payment_screen/${roomId}/${checkInDate}/${checkOutDate}/${guestCount.value}") // Go to Payment/Booking Overview screen
                        Log.d("BookingScreen", "After navigation command")
                    },
                    content = { Text("Book Room") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
        //Log.d("BookingScreen", "Booking room ${room?.name} with ${guestCount.value} guests from $checkInDate to $checkOutDate")
    }
}
