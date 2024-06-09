package com.example.distributed_hotel_booking.entities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.distributed_hotel_booking.components.ByteArrayImage
import com.example.distributed_hotel_booking.components.ReviewPopup
import com.example.distributed_hotel_booking.data.Booking
import com.example.distributed_hotel_booking.data.Review
import com.example.distributed_hotel_booking.util.getTimeAgo
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
    val checkIn = dateFormat.format(booking.dateRange?.startDate ?: "")
    val checkOut = dateFormat.format(booking.dateRange?.endDate ?: "")
    val dateRangeText = "From $checkIn to $checkOut"

    // Check if the booking end date has passed
    val noReview = booking.review == null || booking.review?.rating == -1

    // State variable to track whether the popup is shown
    var showPopup = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .clickable { }
            .padding(12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
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
                    .clip(shape = RoundedCornerShape(16.dp)),
            )

            Spacer(modifier = Modifier.height(16.dp))

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
            if (noReview) {
                Button(
                    onClick = { showPopup.value = true },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        "Review",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    )
                }
            } else {
                Button(
                    onClick = { showPopup.value = true },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        "View Review",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
            if (showPopup.value) {
                if (noReview) { // Open popup to review
                    ReviewPopup(onReview = { grade, description ->
                        // Create a review object
                        val review =
                            Review(booking.userData!!, grade, description, booking.roomInfo!!)
                        // Pass the review data to the callback function
                        onReviewClick(review)
                    }, showPopup = showPopup)
                } else {
                    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    // Open popup to view and edit/delete tour submitted review
                    Text(
                        text = "Last updated on: ${formatter.format(booking.review?.date!!)}",
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
                        },
                        onDelete = onDeleteClick,
                        onEdit = onEditClick,
                        showPopup = showPopup,
                        isEditing = true
                    )
                }
            }
        }
    }
}


