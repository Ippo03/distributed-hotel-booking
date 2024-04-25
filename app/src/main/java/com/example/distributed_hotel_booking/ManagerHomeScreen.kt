package com.example.distributed_hotel_booking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.magnifier
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.distributed_hotel_booking.ui.theme.Distributed_hotel_bookingTheme

class ManagerHomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Distributed_hotel_bookingTheme {
                // A surface container using the 'background' color from the theme
                    MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val count = remember { mutableStateOf(0) }

        Column {
            Greeting2(name = "Manager")
            Text("Welcome to the Manager Home Screen")
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Android Logo",
                modifier = Modifier.clickable {
                    count.value++
                }
            )
            }
        }
    }
@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Distributed_hotel_bookingTheme {
        MyApp()
    }
}
/*
 * This is the manager home screen of the app
 */
// Should start with a greeting message
// Should prompt to bulk load his rooms
// Option to add a room
// Option to view all bookings
// Option to view bookings for a date grouped by area
