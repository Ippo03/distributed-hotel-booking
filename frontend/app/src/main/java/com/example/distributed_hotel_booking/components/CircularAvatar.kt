package com.example.distributed_hotel_booking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircularAvatar(image: ByteArray) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(Color.Gray)
    ) {
        ByteArrayImage(
            imageBytes = image,
            contentDescription = "User's Profile Picture",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(shape = RoundedCornerShape(8.dp)),
        )
    }
}