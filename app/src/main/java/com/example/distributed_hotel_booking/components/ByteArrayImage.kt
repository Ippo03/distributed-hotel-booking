package com.example.distributed_hotel_booking.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

@Composable
fun ByteArrayImage(imageBytes: ByteArray, contentDescription: String?, modifier: Modifier = Modifier) {
    val imageBitmap = byteArrayToBitmap(imageBytes)
    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Fit
        )
    } else {
        // Handle error case if unable to decode byte array
    }
}

private fun byteArrayToBitmap(imageBytes: ByteArray): ImageBitmap? {
    return try {
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        // Handle error case if unable to decode byte array
        null
    }
}