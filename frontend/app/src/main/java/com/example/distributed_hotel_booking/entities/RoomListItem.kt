package com.example.distributed_hotel_booking.entities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.R
import com.example.distributed_hotel_booking.components.ByteArrayImage
import com.example.distributed_hotel_booking.data.Room
import com.example.distributed_hotel_booking.screens.Screen
import com.example.distributed_hotel_booking.viewmodel.SharedViewModel

@Composable
fun RoomListItem(
    room: Room,
    sharedViewModel: SharedViewModel,
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .clickable {
                sharedViewModel.selectedRoom = room
                navController.navigate(Screen.RoomDetailsScreen.route)
            }
            .padding(16.dp)
    ) {
        // Room photo
        ByteArrayImage(
            imageBytes = room.roomImage,
            contentDescription = "Room photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(shape = RoundedCornerShape(16.dp))
        )

        // Spacing
        Spacer(modifier = Modifier.height(16.dp))

        // Room details
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = room.roomName,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Price: ${room.price}€",
                fontSize = 18.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Rating",
                    tint = Color.Yellow,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${room.rating} (${room.noOfReviews} reviews)",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
