package com.example.distributed_hotel_booking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.distributed_hotel_booking.ui.theme.Distributed_hotel_bookingTheme

@Composable
fun BookingScreen() {
    // Temp object to show that the BookingScreen is working
    Surface(color = MaterialTheme.colorScheme.background) {
        Text(
            text = "Booking Screen",
            modifier = Modifier.fillMaxSize()
        )
    }
}