package com.example.distributed_hotel_booking.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.distributed_hotel_booking.R

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    spaceBetween: Dp = 0.dp
) {

    val image = ImageBitmap.imageResource(id = R.drawable.half_star)
    val imageFull = ImageBitmap.imageResource(id = R.drawable.star)

    val totalCount = 5

    val height = LocalDensity.current.run { image.height.toDp() }
    val width = LocalDensity.current.run { image.width.toDp() }
    val space = LocalDensity.current.run { spaceBetween.toPx() }
    val totalWidth = width * totalCount + spaceBetween * (totalCount - 1)


    Box(
        modifier
            .width(totalWidth)
            .height(height)
            .drawBehind {
                drawRating(rating, image, imageFull, space)
            })
}
private fun DrawScope.drawRating(
    rating: Float,
    image: ImageBitmap,
    imageFull: ImageBitmap,
    space: Float
) {

    val totalCount = 5

    val imageWidth = image.width.toFloat()
    val imageHeight = size.height

    val reminder = rating - rating.toInt()
    val ratingInt = (rating - reminder).toInt()

    // Draw full stars
    for (i in 0 until ratingInt) {
        val start = imageWidth * i + space * i
        drawImage(
            image = imageFull,
            topLeft = Offset(start, 0f)
        )
    }

    // Draw half star if there is a remainder
    if (reminder > 0) {
        val start = imageWidth * ratingInt + space * ratingInt
        drawImage(
            image = image,
            topLeft = Offset(start, 0f)
        )
    }

    // Draw remaining empty stars
    for (i in ratingInt + 1 until totalCount) {
        val start = imageWidth * i + space * i
        drawImage(
            image = image,
            topLeft = Offset(start, 0f)
        )
    }
}