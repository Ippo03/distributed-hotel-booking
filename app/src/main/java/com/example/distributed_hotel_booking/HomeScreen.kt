package com.example.distributed_hotel_booking

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen() {
    // Temp object to show that the HomeScreen is working
    val navController = rememberNavController()
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
        Button(
            onClick = {
                // Assuming you have a NavController instance named navController
                navController.navigate("Screen")
            }
        ) {
            Text("Navigate to Other Screen")
        }
    }
}


    // This is the home screen of the app - LOGIN
    // It should contain two buttons "Enter as User" and "Enter as Manager"
