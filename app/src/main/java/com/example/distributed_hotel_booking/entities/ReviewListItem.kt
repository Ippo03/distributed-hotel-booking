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
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.distributed_hotel_booking.R
import com.example.distributed_hotel_booking.components.CircularAvatar
import com.example.distributed_hotel_booking.components.RatingBar
import com.example.distributed_hotel_booking.util.getProfilePicture


@Composable
fun ReviewListItem(
    review: Review,
    onReviewClick: (Review) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onReviewClick(review) }
            .padding(16.dp)
    ) {
        // Avatar
//      CircularAvatar(imageResource = getProfilePicture(review.userData.profilePicture))
        CircularAvatar(imageResource = getProfilePicture("man"))

        Text( //PLACEHOLDER AS OF NOW
            text = "User's Name",
//            text = review.userData.username, //review.userId, // Display the userId -> TODO: ADD IT TO THE REVIEW VIA THE BOOKING
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(16.dp)) // Add space between avatar and content

        Column {
            // Star rating
            Row {
                RatingBar(
                    modifier = Modifier,
                    rating = review.rating.toFloat(),
                    spaceBetween = 6.dp
                )
            }

            Spacer(modifier = Modifier.height(8.dp)) // Add space between star rating and comment

            // Comment
            Text(
                text = review.comment,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}