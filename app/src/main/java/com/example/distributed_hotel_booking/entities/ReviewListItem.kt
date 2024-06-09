package com.example.distributed_hotel_booking.entities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.distributed_hotel_booking.data.Review
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.distributed_hotel_booking.components.CircularAvatar
import com.example.distributed_hotel_booking.components.RatingBar
import com.example.distributed_hotel_booking.data.UserData
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ReviewListItem(
    currentUser: UserData,
    review: Review,
    onReviewClick: (Review) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onReviewClick(review) }
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Avatar
        CircularAvatar(image = review.userData.profilePicture)

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // User Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (review.userData.username == currentUser.username) "You" else review.userData.username,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (review.userData.username == currentUser.username) Color.Red else Color.Black,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Date formatter to display date in a specific format
                val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                Text(
                    text = "Last updated on: ${formatter.format(review.date)}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Rating
            Row {
                RatingBar(
                    modifier = Modifier,
                    rating = review.rating.toFloat(),
                    spaceBetween = 6.dp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Comment
            Text(
                text = review.comment,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}