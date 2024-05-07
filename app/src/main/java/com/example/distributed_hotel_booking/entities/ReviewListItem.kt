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
import com.example.distributed_hotel_booking.components.RatingBar


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
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Gray) // Placeholder color
        ) {
            // Placeholder content for avatar
        }
//        Image(
//            painter = rememberImagePainter(review.userAvatarUrl),
//            contentDescription = null,
//            modifier = Modifier
//                .size(48.dp)
//                .clip(CircleShape)
//                .border(1.dp, Color.Gray, CircleShape) // Add border to avatar
//        )

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
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}