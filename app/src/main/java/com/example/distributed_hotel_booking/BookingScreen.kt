package com.example.distributed_hotel_booking

import android.os.Bundle
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
import androidx.compose.runtime.Composable
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
    val room = DataProvider.getRoomById(roomId)
    val dateTime = LocalDateTime.now()
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Booking Screen",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
            Row {
                Text(
                    text = room?.name ?: "Room not found",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
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
                    showModeToggle = false,
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
                        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        if (endDate != null && startDate != null) {
                            Text("Selected dates: ${startDate.format(formatter)} - ${endDate.format(formatter)}")
                        }
                    },
                    //headline = { Text("Select the check-in and check-out dates", Modifier.size(150.dp)) },
                    dateValidator = { date ->
                        val localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
                        localDate.isAfter(dateTime.toLocalDate().minusDays(1))
                    },
                    modifier = Modifier.animateContentSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .border(4.dp, MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .size(width = 350.dp, height =400.dp)
                )
            }
            Row {
                Button(
                    onClick = { /* TODO: Handle click */ }, // Go to Purchase screen
                    content = { Text("Book Room") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}
