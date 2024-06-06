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
        // Clear the roomsList in the SharedViewModel
        sharedViewModel.roomsList.clear()
        // Pop the UserBookingsScreen from the back stack to return to the HomeScreen
        navController.popBackStack()
//        // Navigate back to the HomeScreen
//        navController.navigate(Screen.HomeScreen.route)
    }


    LaunchedEffect(navController) {
        sharedViewModel.updateBookings()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Bookings:",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        //TODO: Get user bookings from the backend using the user's UserData I guess (or can we have them in the sharedViewModel?)
        LazyColumn {
            items(sharedViewModel.userBookings) { booking ->
                BookingListItem(
                    booking = booking, onReviewClick = { review ->
                        sharedViewModel.updateSelectedBooking(booking)
                        viewModel.onReview(navController, sharedViewModel, context, review) },
                    onDeleteClick = { review -> viewModel.onDelete(navController, sharedViewModel, context, review)},
                    onEditClick = { review -> viewModel.onUpdate(navController, sharedViewModel, context, review)})
                Divider() // Add divider between booking items
            }
        }
    }
}