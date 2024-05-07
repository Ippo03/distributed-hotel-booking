package com.example.distributed_hotel_booking.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.data.DataProvider
import com.example.distributed_hotel_booking.entities.BookingListItem

@Composable
fun UserBookingsScreen(navController: NavController) {
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

        val userId = "1"
        val userBookings = remember { DataProvider.getBookingsByUserId(userId) }

        LazyColumn {
            items(userBookings) { booking ->
                BookingListItem(booking = booking, onReviewClick = {})
                Divider() // Add divider between booking items
            }
        }
    }
}
