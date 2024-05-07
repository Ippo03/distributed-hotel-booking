package com.example.distributed_hotel_booking.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

// Saw from this YT Video: https://youtu.be/uggF_O4xe4I
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePicker(
    dateTime: LocalDateTime,
    focusRequester: FocusRequester,
    input: Boolean = false, //NOT USED NOW
    onDateSelected: (String?, String?) -> Unit,
    small: Boolean = false,
) {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val displayFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")
    val dateRangePickerState = DateRangePickerState(
        initialSelectedStartDateMillis = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
        initialDisplayedMonthMillis = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
        initialSelectedEndDateMillis = dateTime.plusDays(3).toInstant(ZoneOffset.UTC).toEpochMilli(),
        initialDisplayMode = if (input) DisplayMode.Input else DisplayMode.Picker, // ALWAYS PICKER AS OF NOW
        yearRange = (dateTime.year .. dateTime.year + 1)
    )
    if (small) { // For UserHomeScreen
        DateRangePicker(
            state = dateRangePickerState,
            showModeToggle = false,
            title = null,
            headline = null,
            dateValidator = { date ->
                val localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
                localDate.isAfter(dateTime.toLocalDate().minusDays(1))
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth(1.0f) // Fill 50% of the available width
                .fillMaxHeight(0.4f) // Fill 50% of the available height
        )
    } else { // For BookingScreen
        DateRangePicker(
            state = dateRangePickerState,
            showModeToggle = false,
            title = { Text(text = " Choose Your Stay:") },
            headline = {
                val startDate = dateRangePickerState.selectedStartDateMillis?.let {
                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                }
                val endDate = dateRangePickerState.selectedEndDateMillis?.let {
                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                }
                if (endDate != null && startDate != null) {
                    val checkInDate = startDate.format(formatter)
                    val checkOutDate = endDate.format(formatter)
                    onDateSelected(checkInDate, checkOutDate)
                    Text("${startDate.format(displayFormatter)} - ${endDate.format(displayFormatter)}", maxLines = 1, textAlign = TextAlign.Center)
                }
            },
            dateValidator = { date ->
                val localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
                localDate.isAfter(dateTime.toLocalDate().minusDays(1))
            },
            modifier = Modifier
                .focusRequester(focusRequester)
        )
    }
}