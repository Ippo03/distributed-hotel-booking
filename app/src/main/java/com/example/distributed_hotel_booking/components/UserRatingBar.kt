package com.example.distributed_hotel_booking.components

import com.example.distributed_hotel_booking.R
import android.view.MotionEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun UserRatingBar(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    ratingState: MutableState<Int> = remember { mutableIntStateOf(0) },
    selectedColor: Color = Color(0xFFFFD700), // Gold
    unselectedColor: Color = Color(0xFF000000) // Black
) {
    Row(modifier = modifier.wrapContentSize()) {
        for (value in 1..5) {
            StarIcon(
                size = size,
                ratingValue = value,
                ratingState = ratingState,
                selectedColor = selectedColor,
                unselectedColor = unselectedColor
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StarIcon(
    size: Dp,
    ratingState: MutableState<Int>,
    ratingValue: Int,
    selectedColor: Color,
    unselectedColor: Color
) {
    val tint by animateColorAsState(
        targetValue = if (ratingValue <= ratingState.value) selectedColor else unselectedColor,
        label = ""
    )

    val painter = if (ratingValue <= ratingState.value) {
        painterResource(id = R.drawable.star)
    } else {
        painterResource(id = R.drawable.empty_star)
    }

    Icon(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        ratingState.value = ratingValue
                    }
                }
                true
            },
        tint = tint
    )
}

@Preview
@Composable
fun PreviewUserRatingBar() {
    val ratingState = remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UserRatingBar(ratingState = ratingState)
    }
}