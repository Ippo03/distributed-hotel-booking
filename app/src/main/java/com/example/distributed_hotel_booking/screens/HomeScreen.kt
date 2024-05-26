package com.example.distributed_hotel_booking.screens

import com.example.distributed_hotel_booking.components.DateRangePicker
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.components.GridSelector
import com.example.distributed_hotel_booking.components.SimpleDropdown
import com.example.distributed_hotel_booking.components.UserRatingBar
import com.example.distributed_hotel_booking.data.DateRange
import com.example.distributed_hotel_booking.data.Review
import com.example.distributed_hotel_booking.data.Room
import com.example.distributed_hotel_booking.data.SearchFilter
import com.example.distributed_hotel_booking.entities.RoomListItem
import com.example.distributed_hotel_booking.util.getMaxDate
import com.example.distributed_hotel_booking.util.getResourceId
import com.example.distributed_hotel_booking.util.getToday
import com.example.distributed_hotel_booking.util.parseDate
import com.example.distributed_hotel_booking.viewmodel.HomeViewModel
import com.example.distributed_hotel_booking.viewmodel.SharedViewModel
import java.math.BigDecimal
import java.time.LocalDateTime


@SuppressLint("ResourceType", "UnrememberedMutableState")
@Composable
fun HomeScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val viewModel: HomeViewModel = viewModel();
    //viewModel.updateRoomsList(navController, sharedViewModel)

//    val calendar = Calendar.getInstance()
    val dateTime = LocalDateTime.now()
    val focusRequester = remember { FocusRequester() }
    val clearFilters = remember { mutableStateOf(false) }
    val searchFilter =
        remember { mutableStateOf(SearchFilter("", DateRange(getToday(), getMaxDate()), "Athens", 1, BigDecimal.ZERO, 0)) }

    // TEMP CODE FOR DEBUUGING
    val room = Room(
        "8",
        "Presidential Suite",
        DateRange(parseDate("20-05-2024"), parseDate("25-06-2024")),
        1,
        BigDecimal.ZERO,
    )
    val review = remember { mutableStateOf(Review(0, room, 0, "")) }
    // TEMP CODE FOR DEBUUGING

    // Filter selected values
    val searchQuery = remember { mutableStateOf("") }
    var selectedStartDateText = remember { mutableStateOf("") }
    var selectedEndDateText = remember { mutableStateOf("") }
    val selectedArea = remember { mutableStateOf("Athens") }
    val selectedRatingState = remember { mutableIntStateOf(0) }
    var selectedGuests by remember { mutableIntStateOf(1) }
    var selectedPriceRange by remember { mutableStateOf(0f) }

    // IconButton to open dropdown
    var showMenu by remember { mutableStateOf(false) }
    // Handle the search result list visibility
    val showRoomsList = remember { mutableStateOf(true) }
    // Fetching current year, month and day

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
                    text = "Hello, ${sharedViewModel.username.value}!",
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
                                        viewModel.onLogout(navController, sharedViewModel)
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
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
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
                    selectedStartDateText = selectedStartDateText,
                    selectedEndDateText = selectedEndDateText,
                    onDateSelected = { checkIn, checkOut ->
                        selectedStartDateText = (checkIn ?: selectedStartDateText) as MutableState<String>
                        selectedEndDateText = (checkOut ?: selectedEndDateText) as MutableState<String>
                    }
                )
            }
            //TODO:
            // MAYBE COULD HAVE THE DATE RANGE PICKER SCROLL TO THE DEFAULT DATES WHEN THE CLEAR FILTERS BUTTON IS PRESSED -> LAZYCOLUMN, scrollToItem(atIndex)
            // Possible Solution for DateRange highlighter bug. First Scroll to an older/unavailable date and then immediately(next line of code) scroll to the default/current date (today + 3 days).
        }
        item {
            Column(modifier = Modifier.padding(16.dp)) {
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
                        items = listOf("Athens", "Thessaloniki", "Heraclio"),
                        selectedItem = selectedArea
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    // Rating bar and Slider in a Column
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                                    .offset(
                                        x = (selectedPriceRange / 500f * 60.dp) - 2.dp,
                                        y = 4.dp
                                    ) // or -4.dp and 2.dp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Grid for selecting the number of guests
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    GridSelector(
                        rows = 1,
                        columns = 4,
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
                }
            }
        }
        item {
            Row {
                // Button for search
                Button(
                    onClick = {
                        Log.d("Dates", selectedStartDateText.value + " " + selectedEndDateText.value)
                        // Create a SearchFilter object with the selected values
                        searchFilter.value = SearchFilter(
                            roomName = searchQuery.value,
                            dateRange = DateRange(parseDate(selectedStartDateText.value), parseDate(selectedEndDateText.value)),
                            area = selectedArea.value,
                            noOfGuests = selectedGuests,
                            rating = BigDecimal.valueOf(selectedRatingState.value.toDouble()),
                            price = selectedPriceRange.toInt(),
                        )
                        showRoomsList.value = true // Show the rooms list
                        // Print the searchFilter object *APPEARS IN LOGCAT*
                        println(searchFilter.value)
                        viewModel.searchFilter = searchFilter.value
                        viewModel.onSearch(navController, sharedViewModel)
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
                        selectedStartDateText.value = ""
                        selectedEndDateText.value = ""
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
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Hotels Found:",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
            }
        }
        item {
            var commentTextState by remember { mutableStateOf(TextFieldValue("")) }

            Column(modifier = Modifier.padding(16.dp)) {
                Text("Rate this room:", fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))

                Text("Leave a comment:", fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
                BasicTextField(
                    value = commentTextState,
                    onValueChange = { newComment -> commentTextState = newComment },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(8.dp)
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Handle the submit action, e.g., send the review to a server or update the state
                        println("Rating: $selectedRatingState, Comment: ${commentTextState.text}")
                        viewModel.review =
                            Review(
                                sharedViewModel.userId.value,
                                room,
                                selectedRatingState.intValue,
                                commentTextState.text
                            )
                        viewModel.onReview(navController, viewModel)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Submit")
                }
            }
        }
         items(items = sharedViewModel.roomsList) { room ->
             Log.d("Room", room.toString())
            RoomListItem(
                room = room,
                sharedViewModel = sharedViewModel,
                navController = navController,
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

//@Preview
//@Composable
//fun UserHomeScreenPreview() {
//    UserHomeScreen(navController = NavController(LocalContext.current))
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
