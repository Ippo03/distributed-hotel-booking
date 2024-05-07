package com.example.distributed_hotel_booking.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.data.DataProvider
import com.example.distributed_hotel_booking.components.RatingBar
import com.example.distributed_hotel_booking.R
import com.example.distributed_hotel_booking.entities.ReviewListItem

@Composable
fun RoomDetailsScreen(navController: NavController, roomId: String?) {
    val room = DataProvider.getRoomById(roomId)
    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header with decorative elements
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hotel_1),
                    contentDescription = "Room photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
//                        .background(color = Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = "Special Offer!",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Room details
            Text(
                text = room?.name ?: "Room Name",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = room?.description ?: "Room Description",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Room details section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rating: " + room?.rating.toString(),
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 16.dp)
                )
                if (room != null) {
                    RatingBar(
                        modifier = Modifier,
                        rating = room.rating,
                        spaceBetween = 8.dp
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter = painterResource(id = R.drawable.guests),
                    contentDescription = "Guests",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = (room?.guests).toString() + " Guests",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Book button
            Button(
                onClick = { navController.navigate("booking_screen/${roomId}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Book Now",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // Reviews section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = "Reviews",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                // Review list
                for (review in DataProvider.getReviewsByRoomId(room!!.id)) {
                    ReviewListItem(review = review, onReviewClick = { /* Handle review click */ })
                }
            }
        }
    }
}

