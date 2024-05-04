package com.example.distributed_hotel_booking

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.DatePicker
import android.widget.RatingBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.data.DataProvider
import com.example.distributed_hotel_booking.data.Room


@SuppressLint("ResourceType")
@Composable
fun UserHomeScreen(navController: NavController) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

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
                items = listOf("Athens", "Thessaloniki", "Heraklion"),
                selectedItem = selectedArea
            )

            // Column for selecting the number of guests
            Column(
                modifier = Modifier.weight(1f), // Take up available space
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedGuests == 1,
                        onClick = { selectedGuests = 1 }
                    )
                    Text("1")

                    RadioButton(
                        selected = selectedGuests == 2,
                        onClick = { selectedGuests = 2 }
                    )
                    Text("2")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedGuests == 3,
                        onClick = { selectedGuests = 3 }
                    )
                    Text("3")

                    RadioButton(
                        selected = selectedGuests == 4,
                        onClick = { selectedGuests = 4 }
                    )
                    Text("4")
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
            onClick = { /* Do something when search button is clicked */ },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Search")
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Hotels Found:", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp))
            RoomsList(navController, rooms = DataProvider.roomsList)
        }
    }
}

@Composable
fun RoomsList(
    navController: NavController,
    modifier: Modifier = Modifier,
    rooms: List<Room>
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = rooms) { room ->
            RoomListItem(
                room = room,
                navController = navController,
                onItemClick = {
                    // Navigate to the room details screen and pass the roomId as an argument
                    navController.navigate("${Screen.RoomDetailsScreen.route}/${room.id}")
//                    )
                }
            )
        }
    }
}

@Composable
fun RoomListItem(
    room: Room,
    navController: NavController,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        // Room photo
        Image(
            painter = painterResource(id = R.drawable.booking_logo), // Replace R.drawable.booking_logo with your actual image resource
            contentDescription = "Room photo",
            modifier = Modifier.size(68.dp)
        )

        // Spacing
        Spacer(modifier = Modifier.width(16.dp))

        // Room details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = room.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = room.description,
                fontSize = 14.sp
            )
        }

        // View button
        Button(
            onClick = {navController.navigate("room_details_screen/${room.id}")},
            modifier = Modifier.align(Alignment.CenterVertically),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(text = "View", color = Color.White)
        }
    }
}

@Composable
fun SimpleDropdown(
    items: List<String>,
    selectedItem: MutableState<String>,
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.width(120.dp) // Set a fixed width for the button
        ) {
            Text(
                text = selectedItem.value,
                fontSize = 12.sp // Set the font size to a smaller value (adjust as needed)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            fontSize = 12.sp // Set the font size to a smaller value (adjust as needed)
                        )
                    },
                    onClick = {
                        selectedItem.value = item
                        expanded = false
                    },
                    modifier = Modifier.width(130.dp)
                )
            }
        }
    }
}


@Composable
private fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    spaceBetween: Dp = 0.dp
) {

    val image = ImageBitmap.imageResource(id = R.drawable.half_star)
    val imageFull = ImageBitmap.imageResource(id = R.drawable.star)

    val totalCount = 5

    val height = LocalDensity.current.run { image.height.toDp() }
    val width = LocalDensity.current.run { image.width.toDp() }
    val space = LocalDensity.current.run { spaceBetween.toPx() }
    val totalWidth = width * totalCount + spaceBetween * (totalCount - 1)


    Box(
        modifier
            .width(totalWidth)
            .height(height)
            .drawBehind {
                drawRating(rating, image, imageFull, space)
            })
}

private fun DrawScope.drawRating(
    rating: Float,
    image: ImageBitmap,
    imageFull: ImageBitmap,
    space: Float
) {

    val totalCount = 5

    val imageWidth = image.width.toFloat()
    val imageHeight = size.height

    val reminder = rating - rating.toInt()
    val ratingInt = (rating - reminder).toInt()

    for (i in 0 until totalCount) {

        val start = imageWidth * i + space * i

        drawImage(
            image = image,
            topLeft = Offset(start, 0f)
        )
    }

    drawWithLayer {
        for (i in 0 until totalCount) {
            val start = imageWidth * i + space * i
            // Destination
            drawImage(
                image = imageFull,
                topLeft = Offset(start, 0f)
            )
        }

        val end = imageWidth * totalCount + space * (totalCount - 1)
        val start = rating * imageWidth + ratingInt * space
        val size = end - start

        // Source
        drawRect(
            Color.Transparent,
            topLeft = Offset(start, 0f),
            size = Size(size, height = imageHeight),
            blendMode = BlendMode.SrcIn
        )
    }
}

private fun DrawScope.drawWithLayer(block: DrawScope.() -> Unit) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        block()
        restoreToCount(checkPoint)
    }
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
