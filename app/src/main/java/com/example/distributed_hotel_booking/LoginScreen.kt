package com.example.distributed_hotel_booking

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen() {
    // Temp object to show that the HomeScreen is working
    val navController = rememberNavController()
    Surface(color = Color.White) {
        val expanded = remember { mutableStateOf(false) }
        val extraPadding = if (expanded.value) 48.dp else 0.dp
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row {
                Text(
                    text = "Welcome to Hotel Booking !",
                    color = Color.Black,
                    fontSize = 24.sp,
                )
            }
            // Display a welcome message
            Row( modifier = Modifier.padding(100.dp)
            ){
                Image(
                    painter = painterResource(id = R.drawable.booking_logo),
                    contentDescription = "app logo"
                )
            }
            Column() {

            }
            Row(modifier = Modifier
                .padding(100.dp)
                .clickable(
                    onClick = {
                        Log.d("HomeScreen", "Enter as User")
                    })
            ) {
                Button(
                    onClick = {
                        // Assuming you have a NavController instance named navController
                        navController.navigate("UserHomeScreen")

                    }
                ) {
                    Text("Enter as User")
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
        // This is the home screen of the app - LOGIN
        // It should contain two buttons "Enter as User" and "Enter as Manager"
