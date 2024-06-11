package com.example.distributed_hotel_booking.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.entities.BookingListItem
import com.example.distributed_hotel_booking.viewmodel.ReviewViewModel
import com.example.distributed_hotel_booking.viewmodel.SharedViewModel

@Composable
fun UserBookingsScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val viewModel = ReviewViewModel()

    // Handle the back button press
    BackHandler {
        // Clear the userBookings in the SharedViewModel
        sharedViewModel.userBookings.clear()
        // Clear the roomsList in the SharedViewModel
        sharedViewModel.roomsList.clear()
        // Pop the UserBookingsScreen from the back stack to return to the HomeScreen
        navController.popBackStack()
    }


    LaunchedEffect(navController) {
        sharedViewModel.updateBookings()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (sharedViewModel.userBookings.isEmpty()) {
            item {
                Text(
                    text = "You have no bookings",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 16.dp),
                )
            }
        } else {
            item {
                Text(
                    text = "My Bookings:",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(sharedViewModel.userBookings) { booking ->
                BookingListItem(
                    booking = booking, onReviewClick = { review ->
                        sharedViewModel.updateSelectedBooking(booking)
                        viewModel.onReview(navController, sharedViewModel, context, review)
                    },
                    onDeleteClick = { review ->
                        viewModel.onDelete(
                            navController,
                            sharedViewModel,
                            context,
                            review
                        )
                    },
                    onEditClick = { review ->
                        viewModel.onUpdate(
                            navController,
                            sharedViewModel,
                            context,
                            review
                        )
                    })
                Divider()
            }
        }
    }
}