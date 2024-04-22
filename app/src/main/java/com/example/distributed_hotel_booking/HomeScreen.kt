package com.example.distributed_hotel_booking

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    // Temp object to show that the HomeScreen is working
    Surface(color = Color.White) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Display a text message
            Text(
                text = "Home Screen",
                color = Color.Black,
            )
        }
    }
}


    // This is the home screen of the app
    // It should contain a search bar
    // It should contain a date picker
    // It should contain radio buttons for selecting the number of guests
    // It should contain a rating bar for selecting the minimum rating of the hotel
    // It should contain a button to search for hotels
    // It should contain a list of hotels
    // The user can search for hotels by entering the name, location, check-in date, check-out date, number of guests, and minimum rating
    // The user can view the list of hotels that match the search criteria
    // The user can click on a hotel to view more details about the hotel

