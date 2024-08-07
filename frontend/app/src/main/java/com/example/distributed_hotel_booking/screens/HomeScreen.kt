package com.example.distributed_hotel_booking.screens

import com.example.distributed_hotel_booking.components.DateRangePicker
import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*

import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.components.CircularAvatar
import com.example.distributed_hotel_booking.components.GridSelector
import com.example.distributed_hotel_booking.components.SimpleDropdown
import com.example.distributed_hotel_booking.components.UserRatingBar
import com.example.distributed_hotel_booking.data.DateRange
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
    val viewModel: HomeViewModel = viewModel()

    val dateTime = LocalDateTime.now()
    val focusRequester = remember { FocusRequester() }
    val clearFilters = remember { mutableStateOf(false) }
    val searchFilter = remember {
        mutableStateOf(
            SearchFilter(
                "",
                DateRange(getToday(), getMaxDate()),
                "Athens",
                1,
                BigDecimal.ZERO,
                0
            )
        )
    }

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

    // Block the back button press
    BackHandler {
        // Does nothing
    }

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
                CircularAvatar(image = sharedViewModel.userData.profilePicture)

                // Hello user message
                Text(
                    text = "Hello, ${sharedViewModel.userData.username}!",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        letterSpacing = 1.5.sp,
                    ),
                    modifier = Modifier.padding(start = 14.dp)
                )

                // Spacer to push the icon to the right
                Spacer(modifier = Modifier.weight(1f))

                // IconButton to open dropdown
                IconButton(
                    onClick = { showMenu = !showMenu },
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "Open menu")
                }

                // Dropdown menu
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
                                    .background(Color.Transparent)
                                    .border(0.5.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
                                    .clip(MaterialTheme.shapes.medium)
                            ) {
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
                        selectedStartDateText =
                            (checkIn ?: selectedStartDateText) as MutableState<String>
                        selectedEndDateText =
                            (checkOut ?: selectedEndDateText) as MutableState<String>
                    }
                )
            }
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
                        items = listOf("Athens", "Thessaloniki", "Heraclion", "Patras"),
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
                                valueRange = 0f..500f,
                                steps = 10,
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
                                    )
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
                        // Create a SearchFilter object with the selected values
                        searchFilter.value = SearchFilter(
                            roomName = searchQuery.value,
                            dateRange = DateRange(
                                parseDate(selectedStartDateText.value),
                                parseDate(selectedEndDateText.value)
                            ),
                            area = selectedArea.value,
                            noOfGuests = selectedGuests,
                            rating = BigDecimal.valueOf(selectedRatingState.value.toDouble()),
                            price = selectedPriceRange.toInt(),
                        )
                        showRoomsList.value = true
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
                        clearFilters.value = !clearFilters.value
                        showRoomsList.value = false
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Clear Filters")
                }
            }
        }
        if (sharedViewModel.roomsList.isEmpty()) {
            // Display "Explore" message when the list is empty
            item {
                Text(
                    text = "No Rooms. Explore!",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
            }
        } else {
            item {
                Text(
                    text = "Hotels Found:",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
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
}