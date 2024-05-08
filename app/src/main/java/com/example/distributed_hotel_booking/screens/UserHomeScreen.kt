package com.example.distributed_hotel_booking.screens

import com.example.distributed_hotel_booking.components.DateRangePicker
import android.annotation.SuppressLint
import android.util.Log
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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*

import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.components.GridSelector
import com.example.distributed_hotel_booking.components.SimpleDropdown
import com.example.distributed_hotel_booking.components.UserRatingBar
import com.example.distributed_hotel_booking.data.DataProvider
import com.example.distributed_hotel_booking.data.DateRange
import com.example.distributed_hotel_booking.data.Room
import com.example.distributed_hotel_booking.data.SearchFilter
import com.example.distributed_hotel_booking.entities.RoomListItem
import com.example.distributed_hotel_booking.util.getResourceId
import java.time.LocalDateTime


@SuppressLint("ResourceType")
@Composable
fun UserHomeScreen(navController: NavController) {
    val context = LocalContext.current
//    val calendar = Calendar.getInstance()
    val dateTime = LocalDateTime.now()
    val focusRequester = remember { FocusRequester() }
    val clearFilters = remember { mutableStateOf(false) }
    val searchFilter =
        remember { mutableStateOf(SearchFilter("", DateRange("", ""), "Athens", 1, 0f, 0f)) }

    // Filter selected values
    val searchQuery = remember { mutableStateOf("") }
    var selectedStartDateText by remember { mutableStateOf("") }
    var selectedEndDateText by remember { mutableStateOf("") }
    val selectedArea = remember { mutableStateOf("Athens") }
    val selectedRatingState = remember { mutableIntStateOf(0) }
    var selectedGuests by remember { mutableStateOf(1) }
    var selectedPriceRange by remember { mutableStateOf(0f) }

    // IconButton to open dropdown
    var showMenu by remember { mutableStateOf(false) }
    // Handle the search result list visibility
    val showRoomsList = remember { mutableStateOf(true) }
    // Fetching current year, month and day
//    val year = calendar[Calendar.YEAR]
//    val month = calendar[Calendar.MONTH]
//    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

//    // Date Picker for Start Date
//    val startDatePicker = DatePickerDialog(
//        context,
//        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
//            selectedStartDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
//        }, year, month, dayOfMonth
//    )
//
//    // Date Picker for End Date
//    val endDatePicker = DatePickerDialog(
//        context,
//        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
//            selectedEndDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
//        }, year, month, dayOfMonth
//    )

    // Elements of the User Home Screen
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Bar
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Circular Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
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
                        fontSize = 20.sp,
                        letterSpacing = 1.5.sp,
                    ),
                    modifier = Modifier.padding(start = 14.dp) // Add padding between avatar and text
                )

                // Spacer to push the icon to the right
                Spacer(modifier = Modifier.weight(1f))

                // IconButton to open dropdown
                IconButton(
                    onClick = { showMenu = !showMenu },
//                modifier = Modifier.padding(top = 4.dp) // Add padding to the end (right) of the IconButton
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "Open menu")
                }

                // Dropdown menu (WE CAN MAKE THIS CUSTOMIZABLE)
                if (showMenu) {
                    Box(
                        modifier = Modifier
                            .padding(top = 25.dp)
                    ) {
                        Popup(
                            alignment = Alignment.TopEnd,
                            properties = PopupProperties(
                                dismissOnBackPress = true,
                                dismissOnClickOutside = true,
                                focusable = true
                            ),
                            onDismissRequest = { showMenu = false }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .width(140.dp)
                                    .background(Color.Black)
                                    .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.medium)
                                    .clip(MaterialTheme.shapes.medium)
                            ) {
                                // Your dropdown menu content here
                                Button(
                                    onClick = {
                                        // Navigate to "MyBookings" destination
                                        showMenu = false
                                        navController.navigate("user_bookings_screen")
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("My Bookings")
                                }
                                Button(
                                    onClick = {
                                        // Log out action
                                        showMenu = false
                                        // apply backend logic
                                        navController.navigate("login_screen")
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Log out")
                                }
                            }
                        }
                    }
                }
            }
        }
        // Search Bar
        item {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                )
            }

//         // Date Pickers
//         Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(end = 8.dp)
//            ) {
//                Button(
//                    onClick = {
//                        startDatePicker.show()
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(
//                        text = if (selectedStartDateText.isNotEmpty()) {
//                            selectedStartDateText
//                        } else {
//                            "Select start date"
//                        }
//                    )
//                }
//            }
//
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(start = 8.dp) // Add padding to the left
//            ) {
//                Button(
//                    onClick = {
//                        endDatePicker.show()
//                    },
//                    modifier = Modifier.fillMaxWidth() // Increase width of the button
//                ) {
//                    Text(
//                        text = if (selectedEndDateText.isNotEmpty()) {
//                            selectedEndDateText
//                        } else {
//                            "Select end date"
//                        }
//                    )
//                }
//            }
//        }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            // DateRangePicker
            Row {
                // DateRangePicker
                DateRangePicker(
                    dateTime = dateTime,
                    focusRequester = focusRequester,
                    small = true,
                    clearFilters = clearFilters,
                    onDateSelected = { checkIn, checkOut ->
                        selectedStartDateText = checkIn ?: selectedStartDateText
                        selectedEndDateText = checkOut ?: selectedEndDateText
                    }
                )
            }
            //TODO:
            // MAYBE COULD HAVE THE DATE RANGE PICKER SCROLL TO THE DEFAULT DATES WHEN THE CLEAR FILTERS BUTTON IS PRESSED -> LAZYCOLUMN, scrollToItem(atIndex)
            // Possible Solution for DateRange highlighter bug. First Scroll to an older/unavailable date and then immediately(next line of code) scroll to the default/current date (today + 3 days).
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dropdown menu for selecting the area
                SimpleDropdown(
                    items = listOf("Athens", "Thessaloniki", "Heraclio"),  //TODO: Could add to get something from all the Rooms in the App from the DataProvider
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

                Spacer(modifier = Modifier.width(16.dp))

                // Rating bar and Slider in a Column
                Column {
                    // Rating bar
                    UserRatingBar(
                        ratingState = selectedRatingState,
                        size = 16.dp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Slider for the Price Filter
                        Slider(
                            value = selectedPriceRange,
                            onValueChange = { newValue ->
                                selectedPriceRange = newValue
                            },
                            valueRange = 0f..500f, // Define your range according to your needs
                            steps = 10, // Define the number of discrete steps
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Tooltip Text
                        Text(
                            text = "${selectedPriceRange.toInt()} €",
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .offset(x = (selectedPriceRange / 500f * 60.dp) - 2.dp, y = 4.dp) // or -4.dp and 2.dp
                        )
                    }
                }
            }
        }
        item {
            Row {
                // Button for search
                Button(
                    onClick = {
                        // Create a SearchFilter object with the selected values
                        searchFilter.value = SearchFilter(
                            title = searchQuery.value,
                            dateRange = DateRange(selectedStartDateText, selectedEndDateText),
                            area = selectedArea.value,
                            numberOfGuests = selectedGuests,
                            rating = selectedRatingState.value.toFloat(),
                            priceRange = selectedPriceRange,
                        )
                        showRoomsList.value = true // Show the rooms list
                        // Print the searchFilter object *APPEARS IN LOGCAT*
                        println(searchFilter.value)
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Search")
                }
                Spacer(modifier = Modifier.padding(40.dp))
                // Button to clear search filters
                Button(
                    onClick = {
                        // Clear the search filters
                        searchQuery.value = ""
                        selectedStartDateText = ""
                        selectedEndDateText = ""
                        selectedArea.value = "Athens"
                        selectedGuests = 1
                        selectedRatingState.value = 0
                        selectedPriceRange = 0f
                        clearFilters.value = !clearFilters.value // For the DateRangePicker
                        showRoomsList.value = false // Hide the rooms list
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Clear Filters")
                }
            }
        }
        //TODO:
        // MAYBE TO BE SHOWN FULLY ONLY WHEN THE SEARCH BUTTON IS PRESSED, ELSE HIDE IT BUT BE ABLE TO CLOSE IT AND OPEN IT BY SCROLLING
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // List of hotels
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Hotels Found:",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
            }
        }
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

//TODO:
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
