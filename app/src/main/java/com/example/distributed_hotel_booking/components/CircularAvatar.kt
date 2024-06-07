package com.example.distributed_hotel_booking.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CircularAvatar(imageResource: Int) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.Gray) // Placeholder color
    ) {
        Image(
            painter = painterResource(id = imageResource), // Your image resource
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(40.dp)
        )
    }
}