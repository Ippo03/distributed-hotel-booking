package com.example.distributed_hotel_booking.entities

import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.distributed_hotel_booking.components.ByteArrayImage
import com.example.distributed_hotel_booking.components.UserRatingBar
import com.example.distributed_hotel_booking.data.Booking
import com.example.distributed_hotel_booking.data.Review
import com.example.distributed_hotel_booking.data.RoomInfo
import com.example.distributed_hotel_booking.data.UserData
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun BookingListItem(
    booking: Booking,
    onReviewClick: (Review) -> Unit,
    onDeleteClick: (Review) -> Unit,
    onEditClick: (Review) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val checkIn = dateFormat.format(booking.dateRange?.startDate ?: "") // TODO: Fix this - handle null date
    val checkOut = dateFormat.format(booking.dateRange?.endDate ?: "")
    val dateRangeText = "From $checkIn to $checkOut"
    // Check if the booking end date has passed
    val isBookingCompleted = booking.dateRange?.endDate?.before(Calendar.getInstance().time) ?: false
    val noReview = booking.review == null || booking.review?.rating == -1

    // Date formatter to display date in a specific format
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    // State variable to track whether the popup is shown
    var showPopup = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .clickable { /* Handle item click */ }
            .padding(12.dp)
            .fillMaxWidth(),
//        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp) // Custom shape
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Room photo
            ByteArrayImage(
                imageBytes = booking.roomInfo!!.roomImage,
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(16.dp)), // Custom shape
//                contentScale = ContentScale.Crop // Adjust content scale as needed
            )

            Spacer(modifier = Modifier.height(16.dp)) // Add space between photo and content

            // Hotel name
            var roomName = booking.roomInfo?.roomName
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
                text = "Total Amount Paid: ${booking.total} €",
                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Review button (only if the booking is completed)
            //if (isBookingCompleted) { // TODO: Remove for test purposes
            if (noReview) {
                Button(
                    onClick = { showPopup.value = true },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.White), // Custom button colors
                    modifier = Modifier.align(Alignment.End) // Align button to the end of the column
                ) {
                    Text(
                        "Review",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    )
                }
            } else {
                Button(
                    onClick = { showPopup.value = true },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.White), // Custom button colors
                    modifier = Modifier.align(Alignment.End) // Align button to the end of the column
                ) {
                    Text(
                        "View Review",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
            //}
            if (showPopup.value) {
                if (noReview) { // Open popup to review
                    ReviewPopup(onReview = { grade, description ->
                        // Create a review object
                        val review = Review(booking.userData!!, grade, description, booking.roomInfo!!) // In the viewModel before sending the review to the backend, we will add the room info to the review object from the sharedViewModel
                        // Pass the review data to the callback function
                        onReviewClick(review)
                    }, showPopup = showPopup)
                } else{ // Open popup to view and edit/delete tour submitted review
                    Text(
                        text = " Last updated on : ${formatter.format(booking.review?.date)}.",
                        style = TextStyle(fontSize = 8.sp, color = Color.Gray),
                        maxLines = 1,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 4.dp, bottom = 2.dp)
                            .padding(horizontal = 2.dp)
                    )
                    ReviewPopup(review = booking.review,
                        onReview = { _, _ ->
                        // Create a review object
                        val review = booking.review!!
                        // Pass the review data to the callback function
                        onReviewClick(review)
                    }, onDelete = onDeleteClick, onEdit = onEditClick, showPopup = showPopup, isEditing = true)
                }

            }
        }
    }
}

@Composable
fun ReviewPopup(
    review: Review? = null,
    onReview: (Int, String) -> Unit,
    onDelete: (Review) -> Unit = {},
    onEdit: (Review) -> Unit = {},
    showPopup: MutableState<Boolean>,
    isEditing: Boolean = false
) {
    // State variables to hold user input
    var grade = remember { mutableIntStateOf(review?.rating ?: 0) }
    var description by remember { mutableStateOf(review?.comment ?: "") }

    // Add some padding around the popup
    Spacer(modifier = Modifier.height(6.dp))

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Leave a Review",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Rate your experience",
                style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            UserRatingBar(ratingState = grade)

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isEditing) {
                Row (Modifier.fillMaxWidth()) {
                    Button(
                        modifier = Modifier
                            //.padding(end = 8.dp)
                            .weight(1.2f), // Increase weight for more space
                        onClick = {
                            showPopup.value = false
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                    ) {
                        Text("Cancel", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold))
                    }
                    Spacer(modifier = Modifier.width(4.dp)) // Add space between the buttons
                    Button(
                        modifier = Modifier
                            //.padding(start = 6.dp, end = 6.dp)
                            .weight(1f), // Distribute the available space equally among the buttons,
                        onClick = {
                            onDelete(Review(UserData(), grade.value, description.trim(), RoomInfo())) // Invalid review to indicate deletion in the backend -> Review in this booking turned to NULL.
                            showPopup.value = false
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                    ) {
                        Text("Delete", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold))
                    }
                    Spacer(modifier = Modifier.width(4.dp)) // Add space between the buttons
                    Button(
                        modifier = Modifier
                            //.padding(start = 8.dp)
                            .weight(1.2f), // Increase weight for more space
                        onClick = {
                            onEdit(Review(review?.userData!!, grade.value, description.trim(), review?.roomInfo!!)) // Update the review in the backend
                            showPopup.value = false
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                    ) {
                        Text("Update", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold))
                    }
                }
            } else {
                Row {
                    Button(
                        modifier = Modifier.weight(1f), // Distribute the available space equally among the buttons,
                        onClick = {
                            grade.value = 0
                            description = ""
                            showPopup.value = false
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White), // Custom button colors
                    ) {
                        Text("Cancel", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold))
                    }
                    Spacer(modifier = Modifier.width(10.dp)) // Add space between the buttons
                    Button(
                        modifier = Modifier.weight(1f), // Distribute the available space equally among the buttons,
                        onClick = {
                            // Pass the user input to the callback function
                            onReview(grade.value, description.trim())
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White), // Custom button colors
                    ) {
                        Text("Submit", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }

        // Add some padding below the popup
        Spacer(modifier = Modifier.height(10.dp))
    }
}


