package com.example.distributed_hotel_booking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.example.distributed_hotel_booking.screens.Navigator
import com.example.distributed_hotel_booking.viewmodel.SharedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sets the content of the initial activity
        setContent {
            val sharedViewModel = remember { SharedViewModel() }
            Navigator(sharedViewModel)
        }
    }
}