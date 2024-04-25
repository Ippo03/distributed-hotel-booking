package com.example.distributed_hotel_booking

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import java.util.Date

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton

@SuppressLint("ResourceType")
@Composable
fun UserHomeScreen() {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedStartDateText by remember { mutableStateOf("") }
    var selectedEndDateText by remember { mutableStateOf("") }

    // Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    // Date Picker for Start Date
    val startDatePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedStartDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        }, year, month, dayOfMonth
    )

    // Date Picker for End Date
    val endDatePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedEndDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        }, year, month, dayOfMonth
    )

    val names = List(1000) { "Hello, World!" }

    // Temp object to show that the UserHomeScreen is working
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val searchQuery = remember { mutableStateOf("") }

        Text(
            text = "Welcome to Hotel Booking!",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Search Bar
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchQuery.value,
                onValueChange = { newValue -> searchQuery.value = newValue },
                label = { Text("Search") },
                leadingIcon = {
                    IconButton(onClick = { /* Do something when search button is clicked */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Date Pickers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (selectedStartDateText.isNotEmpty()) {
                        "Selected start date is $selectedStartDateText"
                    } else {
                        "Please pick a start date"
                    }
                )

                Button(
                    onClick = {
                        startDatePicker.show()
                    }
                ) {
                    Text(text = "Select start date")
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (selectedEndDateText.isNotEmpty()) {
                        "Selected end date is $selectedEndDateText"
                    } else {
                        "Please pick an end date"
                    }
                )

                Button(
                    onClick = {
                        endDatePicker.show()
                    }
                ) {
                    Text(text = "Select end date")
                }
            }
        }

        // Dropdown for area
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text("Select the area")
                Spacer(modifier = Modifier.size(8.dp))
                // Dropdown for area
            }

        }
        // rating bar
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text("Select the minimum rating")
            Spacer(modifier = Modifier.size(8.dp))
            // Rating bar
        }
        // Number of guests
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Select the number of guests")
            Spacer(modifier = Modifier.size(8.dp))
            // Radio buttons for selecting the number of guests

        }


        // Button for search
        Button(
            onClick = { /* Do something when search button is clicked */ },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Search")
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "List of hotels will be displayed here")
            Greetings()
        }
    }
}

@Preview
@Composable
fun UserHomeScreenPreview() {
    UserHomeScreen()
}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000) { "$it" }
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Text(text = name)
        }
    }
}

//@Composable
//fun AreaSelectionScreen() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        var expanded by remember { mutableStateOf(false) }
//        var selectedArea by remember { mutableStateOf("") }
//
//        val areas = listOf("Area 1", "Area 2", "Area 3")
//
//        OutlinedButton(
//            onClick = { expanded = true },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = if (selectedArea.isBlank()) stringResource(id = R.string.select_area) else selectedArea)
//        }
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            areas.forEach { area ->
//                DropdownMenuItem(onClick = {
//                    selectedArea = area
//                    expanded = false
//                }) {
//                    Text(text = area)
//                }
//            }
//        }
//    }
//}



/*
 * This is the user home screen of the app
 */
// It should contain a search bar
// It should contain a date picker
// It should contain radio buttons for selecting the number of guests
// It should contain a rating bar for selecting the minimum rating of the hotel
// It should contain a button to search for hotels
// It should contain a list of hotels
// The user can search for hotels by entering the name, location, check-in date, check-out date, number of guests, and minimum rating
// The user can view the list of hotels that match the search criteria
// The user can click on a hotel to view more details about the hotel
