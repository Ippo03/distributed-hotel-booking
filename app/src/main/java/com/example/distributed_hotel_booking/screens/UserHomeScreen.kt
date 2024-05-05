package com.example.distributed_hotel_booking.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import java.util.*

import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.RadioButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.components.GridSelector
import com.example.distributed_hotel_booking.components.RatingBar
import com.example.distributed_hotel_booking.components.SimpleDropdown
import com.example.distributed_hotel_booking.data.DataProvider
import com.example.distributed_hotel_booking.data.DateRange
import com.example.distributed_hotel_booking.data.Room
import com.example.distributed_hotel_booking.data.SearchFilter
import com.example.distributed_hotel_booking.entities.RoomListItem
import com.example.distributed_hotel_booking.util.getResourceId


@SuppressLint("ResourceType")
@Composable
fun UserHomeScreen(navController: NavController) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val searchFilter = remember { mutableStateOf(SearchFilter("", DateRange("", ""), "Athens", 1, 0f)) }

    // Filter selected values
    var selectedStartDateText by remember { mutableStateOf("") }
    var selectedEndDateText by remember { mutableStateOf("") }
    val selectedArea = remember { mutableStateOf("Athens") }
    val selectedRating = remember { mutableStateOf(0f) }
    var selectedGuests by remember { mutableStateOf(1) }

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

    // Elements of the User Home Screen
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val searchQuery = remember { mutableStateOf("") }

        // App Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth() // Pushes the Row to full left
        ) {
            // Circular Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Gray) // Placeholder color
            ) {
                // Placeholder content for avatar
            }

            // Hello user message
            Text(
                text = "Hello User!",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    letterSpacing = 1.5.sp,
                ),
                modifier = Modifier.padding(start = 14.dp) // Add padding between avatar and text
            )
        }

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
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Button(
                    onClick = {
                        startDatePicker.show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (selectedStartDateText.isNotEmpty()) {
                            selectedStartDateText
                        } else {
                            "Select start date"
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp) // Add padding to the left
            ) {
                Button(
                    onClick = {
                        endDatePicker.show()
                    },
                    modifier = Modifier.fillMaxWidth() // Increase width of the button
                ) {
                    Text(
                        text = if (selectedEndDateText.isNotEmpty()) {
                            selectedEndDateText
                        } else {
                            "Select end date"
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dropdown menu for selecting the area
            SimpleDropdown(
                items = listOf("Athens", "Thessaloniki", "Heraclio"),
                selectedItem = selectedArea
            )

           // Grid for selecting the number of guests
            GridSelector(
                rows = 2,
                columns = 2,
                selectedValue = selectedGuests,
                onValueSelected = { selectedGuests = it }
            ) { value ->
                // Content of each cell
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedGuests == value,
                        onClick = { selectedGuests = value }
                    )
                    Icon(
                        painter = painterResource(id = getResourceId(value.toString())),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            // Rating bar
            RatingBar(
                modifier = Modifier,
                rating = selectedRating.value,
                spaceBetween = 8.dp
            )
        }

        // Button for search
        Button(
            onClick = {
                // Create a SearchFilter object with the selected values
                searchFilter.value = SearchFilter(
                    title = searchQuery.value,
                    dateRange = DateRange(selectedStartDateText, selectedEndDateText),
                    area = selectedArea.value,
                    numberOfGuests = selectedGuests,
                    rating = 4.5f
                )

                // Print the searchFilter object *APPEARS IN LOGCAT*
                println(searchFilter.value)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Search")
        }

        // Add a button to clear search filters

        Spacer(modifier = Modifier.height(16.dp))

        // List of hotels
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Hotels Found:", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
            LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                items(items = DataProvider.roomsList) { room ->
                    RoomListItem(
                        room = room,
                        navController = navController,
                        onItemClick = {
                            // Navigate to the room details screen and pass the roomId as an argument
                            navController.navigate("${Screen.RoomDetailsScreen.route}/${room.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RoomsList(
    navController: NavController,
    modifier: Modifier = Modifier,
    rooms: List<Room>
) {

}

@Preview
@Composable
fun UserHomeScreenPreview() {
    UserHomeScreen(navController = NavController(LocalContext.current))
}


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
