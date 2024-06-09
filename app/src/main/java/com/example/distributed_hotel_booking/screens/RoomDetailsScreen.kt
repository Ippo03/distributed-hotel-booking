package com.example.distributed_hotel_booking.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.components.RatingBar
import com.example.distributed_hotel_booking.R
import com.example.distributed_hotel_booking.components.ByteArrayImage
import com.example.distributed_hotel_booking.data.Booking
import com.example.distributed_hotel_booking.data.Room
import com.example.distributed_hotel_booking.data.UserData
import com.example.distributed_hotel_booking.entities.ReviewListItem
import com.example.distributed_hotel_booking.viewmodel.SharedViewModel

@Composable
fun RoomDetailsScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    // Get the selected room from the shared view model
    val selectedRoom = sharedViewModel.selectedRoom

    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header with decorative elements
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                ByteArrayImage(
                    imageBytes = selectedRoom.roomImage,
                    contentDescription = "Room photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                )
            }

            // Room details
            Text(
                text = selectedRoom.roomName,
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // Room details section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rating: ",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 16.dp)
                )
                if (selectedRoom != null) {
                    selectedRoom.rating?.let {
                        RatingBar(
                            modifier = Modifier,
                            rating = it.toFloat(),
                            spaceBetween = 8.dp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter = painterResource(id = R.drawable.guests),
                    contentDescription = "Guests",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = (selectedRoom.noOfGuests).toString() + " Guests",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Book button
            Button(
                onClick = {
                    // Navigate to booking screen
                    navController.navigate(Screen.BookingScreen.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Book Now",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // Reviews section
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
            if (!hasReviews(sharedViewModel.selectedRoom)) {
                // Display "Search for rooms" message when the list is empty
                item {
                    Text(
                        text = "No Reviews Yet.",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    )
                }
            } else {
                item {
                    // List of reviews
                    Text(
                        text = "Reviews",
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                // Review list
                items(reOrderReviews(selectedRoom.bookings, sharedViewModel.userData)) { booking ->
                    ReviewListItem(
                        currentUser = sharedViewModel.userData,
                        review = booking.review!!,
                        onReviewClick = {}
                    )
                }
                }
            }
        }
    }
}

fun reOrderReviews(bookings: List<Booking>, currentUser: UserData): List<Booking> {
    val bookingsCopy = bookings.toMutableList()
    var currentUserBookings = emptyList<Booking>()
    val iterator = bookingsCopy.iterator()

    while (iterator.hasNext()) {
        val booking = iterator.next()
        if (booking.review?.userData?.username == currentUser.username) {
            iterator.remove()
            currentUserBookings = currentUserBookings.plus(booking)
        }
    }

    currentUserBookings = currentUserBookings.sortedByDescending { it.review?.date }
    val sortedBookings = currentUserBookings.plus(bookingsCopy).sortedByDescending { it.review?.date }
    return sortedBookings
}

fun hasReviews(room: Room): Boolean {
    for (booking in room.bookings) {
        if (booking.review != null) {
            return true
        }
    }
    return false
}

