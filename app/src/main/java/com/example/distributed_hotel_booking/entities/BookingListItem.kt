package com.example.distributed_hotel_booking.entities

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.distributed_hotel_booking.R
import com.example.distributed_hotel_booking.data.Booking
import com.example.distributed_hotel_booking.data.DataProvider
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun BookingListItem(
    booking: Booking,
    onReviewClick: (Booking) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val checkIn = dateFormat.format(booking.dateRange?.startDate ?: "") // TODO: Fix this - handle null date
    val checkOut = dateFormat.format(booking.dateRange?.endDate ?: "")
    val dateRangeText = "From $checkIn to $checkOut"

    // State variable to track whether the popup is shown
    var showPopup by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .clickable { /* Handle item click */ }
            .padding(16.dp)
            .fillMaxWidth(),
//        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp) // Custom shape
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Room photo
            Image(
                painter = painterResource(id = R.drawable.hotel_1), // Replace with actual image resource (booking.room.photoI
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(16.dp)), // Custom shape
                contentScale = ContentScale.Crop // Adjust content scale as needed
            )

            Spacer(modifier = Modifier.height(16.dp)) // Add space between photo and content

            // Hotel name
            var roomName = booking.room?.roomName
            Text(
                text = roomName?:"Room Name",
                style = TextStyle(fontSize = 20.sp, color = Color.Gray),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Date range
            Text(
                text = dateRangeText,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Total amount paid
            Text(
                text = "Total Amount Paid: ${booking.total}",
                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Review button
            Button(
                onClick = { showPopup = true },
                colors = ButtonDefaults.buttonColors(contentColor = Color.White), // Custom button colors
                modifier = Modifier.align(Alignment.End) // Align button to the end of the column
            ) {
                Text(
                    "Review",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                )
            }

            if (showPopup) {
                ReviewPopup(onReviewSubmitted = { grade, description ->
                    // Pass the review data to the callback function
                    onReviewClick(booking)
                    showPopup = false
                })
            }
        }
    }
}

@Composable
fun ReviewPopup(
    onReviewSubmitted: (String, String) -> Unit
) {
    // State variables to hold user input
    var grade by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Add some padding around the popup
    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), // Custom shape
//        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Leave a Review",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                value = grade,
                onValueChange = { grade = it },
                label = { Text("Grade") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Pass the user input to the callback function
                    onReviewSubmitted(grade, description)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Submit")
            }
        }
    }

    // Add some padding below the popup
    Spacer(modifier = Modifier.height(16.dp))
}


