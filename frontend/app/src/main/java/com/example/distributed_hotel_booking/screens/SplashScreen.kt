package com.example.distributed_hotel_booking.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember { Animatable(0f) }
    val showText = remember { Animatable(0f) }

    // Launch the animation when the composable is first drawn
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
        // Delay for a short period before navigating to the next screen
        delay(1000)
        // Navigate to the next screen
        navController.navigate(Screen.LoginScreen.route) {
            // Pop the splash screen off the back stack
            popUpTo(Screen.SplashScreen.route) { inclusive = true }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        // Column to stack Image and Text vertically
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Logo image with animation
            Image(
                painter = painterResource(id = R.drawable.bookaro_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale.value)
            )

            // Delay before showing the text
            LaunchedEffect(Unit) {
                delay(1250)
                showText.animateTo(1f)
            }

            if (showText.value == 1f) {
                Spacer(modifier = Modifier.size(12.dp))

                // Title text
                Text(
                    text = "Book🌴 or Booketo🥊",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
