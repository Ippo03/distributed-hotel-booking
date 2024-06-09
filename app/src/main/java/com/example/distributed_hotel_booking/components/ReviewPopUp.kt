package com.example.distributed_hotel_booking.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.distributed_hotel_booking.data.Review
import com.example.distributed_hotel_booking.data.RoomInfo
import com.example.distributed_hotel_booking.data.UserData

@Composable
fun ReviewPopup(
    review: Review? = null,
    onReview: (Int, String) -> Unit,
    onDelete: (Review) -> Unit = {},
    onEdit: (Review) -> Unit = {},
    showPopup: MutableState<Boolean>,
    isEditing: Boolean = false
) {
    // State variables to hold user input
    var grade = remember { mutableIntStateOf(review?.rating ?: 0) }
    var description by remember { mutableStateOf(review?.comment ?: "") }

    // Add some padding around the popup
    Spacer(modifier = Modifier.height(6.dp))

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Leave a Review",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Rate your experience",
                style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            UserRatingBar(ratingState = grade)

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isEditing) {
                Row (Modifier.fillMaxWidth()) {
                    Button(
                        modifier = Modifier
                            .weight(1.2f),
                        onClick = {
                            showPopup.value = false
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                    ) {
                        Text("Cancel", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            onDelete(Review(UserData(), grade.value, description.trim(), RoomInfo()))
                            showPopup.value = false
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                    ) {
                        Text("Delete", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        modifier = Modifier
                            .weight(1.2f),
                        onClick = {
                            onEdit(Review(review?.userData!!, grade.value, description.trim(), review?.roomInfo!!))
                            showPopup.value = false
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                    ) {
                        Text("Update", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold))
                    }
                }
            } else {
                Row {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            grade.value = 0
                            description = ""
                            showPopup.value = false
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                    ) {
                        Text("Cancel", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // Pass the user input to the callback function
                            onReview(grade.value, description.trim())
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                    ) {
                        Text("Submit", style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }

        // Add some padding below the popup
        Spacer(modifier = Modifier.height(10.dp))
    }
}