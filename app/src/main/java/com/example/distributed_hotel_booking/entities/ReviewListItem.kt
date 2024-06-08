package com.example.distributed_hotel_booking.entities

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.distributed_hotel_booking.data.Review
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.distributed_hotel_booking.R
import com.example.distributed_hotel_booking.components.CircularAvatar
import com.example.distributed_hotel_booking.components.RatingBar
import com.example.distributed_hotel_booking.data.UserData
import com.example.distributed_hotel_booking.util.getProfilePicture
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

        Spacer(modifier = Modifier.width(8.dp)) // Add space between avatar and content

        if (review.userData.username == currentUser.username) {
            Text(
                text = "You",
                fontSize = 18.sp,
                color = Color.Red
            )
        } else {
            Text(
                text = review.userData.username,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.width(20.dp)) // Add space between avatar and content

        Column {
            // Star rating
            Row {
                RatingBar(
                    modifier = Modifier,
                    rating = review.rating.toFloat(),
                    spaceBetween = 6.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Date formatter to display date in a specific format
                val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                Text(
                    text = " Last updated on : ${formatter.format(review.date)}.",
                    style = TextStyle(fontSize = 10.sp, color = Color.Gray),
                )
            }

            Spacer(modifier = Modifier.height(12.dp)) // Add space between star rating and comment

            // Comment
            Text(
                text = review.comment,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
        }
    }
}