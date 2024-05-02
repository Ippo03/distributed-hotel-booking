import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.distributed_hotel_booking.Screen
import com.example.distributed_hotel_booking.data.DataProvider
import com.example.distributed_hotel_booking.data.Room

@Composable
fun RoomDetailsScreen(navController: NavController) {
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
//                Image(
////                    painter = painterResource(id = R.drawable.room_placeholder),
//                    contentDescription = "Room photo",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                        .clip(shape = RoundedCornerShape(8.dp)),
//                    contentScale = ContentScale.Crop
//                )
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
                text = "Luxury Suite",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Experience luxury at its finest in our exquisite suite. With stunning views, premium amenities, and personalized service, your stay will be truly unforgettable.",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Room details section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Icon(
////                    painter = painterResource(id = R.drawable.ic_star),
//                    contentDescription = "Star Icon",
//                    tint = Color(0xFFFED39F),
//                    modifier = Modifier.size(24.dp)
//                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "4.8",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 16.dp)
                )
//                Icon(
////                    painter = painterResource(id = R.drawable.ic_people),
//                    contentDescription = "People Icon",
//                    tint = Color(0xFF6AB7FF),
//                    modifier = Modifier.size(24.dp)
//                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "2 Guests",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Book button
            Button(
                onClick = { /* TODO: Implement booking action */ },
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
        }
    }
}
