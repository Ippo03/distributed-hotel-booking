package com.example.distributed_hotel_booking.entities

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.R
import com.example.distributed_hotel_booking.data.DateRange
import com.example.distributed_hotel_booking.data.Room
import com.example.distributed_hotel_booking.screens.Screen
import com.example.distributed_hotel_booking.viewmodel.HomeViewModel
import com.example.distributed_hotel_booking.viewmodel.SharedViewModel
import java.math.BigDecimal
import java.time.Instant
import java.util.Date

@Composable
fun RoomListItem(
    room: Room,
    sharedViewModel: SharedViewModel,
    navController: NavController,
//    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
//            .clickable(onClick = onItemClick)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        // Room photo
        Image(
            painter = painterResource(id = R.drawable.hotel_1), // Replace R.drawable.booking_logo with your actual image resource
            contentDescription = "Room photo",
            modifier = Modifier.size(68.dp)
        )

        // Spacing
        Spacer(modifier = Modifier.width(16.dp))

        // Room details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = room.roomName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = room.description,
//                fontSize = 14.sp
//            )
        }

        // View button
        Button(
            onClick = {
                sharedViewModel.selectedRoom = room
                Log.d("RoomListItem", "Selected room: $sharedViewModel.selectedRoom")
                navController.navigate(Screen.RoomDetailsScreen.route)
            },
//            onClick = { navController.navigate("room_details_screen/${room.roomId}") },
            modifier = Modifier.align(Alignment.CenterVertically),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(text = "View", color = Color.White)
        }
    }
}