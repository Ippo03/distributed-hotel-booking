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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.Screen
import com.example.distributed_hotel_booking.data.DataProvider
import com.example.distributed_hotel_booking.data.Room

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
//                if (room != null) {
//                    RatingBar(
//                        rating = room.rating,
//                        spaceBetween = 4.dp
//                    )
//                    }
//                }
                Spacer(modifier = Modifier.width(4.dp))
//                Image(
//                    painter = painterResource(id = R.drawable.guests),
//                    contentDescription = "Guests",
//                    modifier = Modifier.size(24.dp)
//                )
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
                onClick = {navController.navigate("booking_screen/${roomId}")},
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

// TODO: Strange error with drawables
// TODO: cool rating bar from stackOverflow https://stackoverflow.com/questions/73948960/jetpack-compose-how-to-create-a-rating-bar

//@Composable
//private fun RatingBar(
//    modifier: Modifier = Modifier,
//    rating: Float,
//    spaceBetween: Dp = 0.dp
//) {
//
//    val image = ImageBitmap.imageResource(id = R.drawable.half_star)
//    val imageFull = ImageBitmap.imageResource(id = R.drawable.star)
//
//    val totalCount = 5
//
//    val height = LocalDensity.current.run { image.height.toDp() }
//    val width = LocalDensity.current.run { image.width.toDp() }
//    val space = LocalDensity.current.run { spaceBetween.toPx() }
//    val totalWidth = width * totalCount + spaceBetween * (totalCount - 1)
//
//
//    Box(
//        modifier
//            .width(totalWidth)
//            .height(height)
//            .drawBehind {
//                drawRating(rating, image, imageFull, space)
//            })
//}
//
//private fun DrawScope.drawRating(
//    rating: Float,
//    image: ImageBitmap,
//    imageFull: ImageBitmap,
//    space: Float
//) {
//
//    val totalCount = 5
//
//    val imageWidth = image.width.toFloat()
//    val imageHeight = size.height
//
//    val reminder = rating - rating.toInt()
//    val ratingInt = (rating - reminder).toInt()
//
//    for (i in 0 until totalCount) {
//
//        val start = imageWidth * i + space * i
//
//        drawImage(
//            image = image,
//            topLeft = Offset(start, 0f)
//        )
//    }
//
//    drawWithLayer {
//        for (i in 0 until totalCount) {
//            val start = imageWidth * i + space * i
//            // Destination
//            drawImage(
//                image = imageFull,
//                topLeft = Offset(start, 0f)
//            )
//        }
//
//        val end = imageWidth * totalCount + space * (totalCount - 1)
//        val start = rating * imageWidth + ratingInt * space
//        val size = end - start
//
//        // Source
//        drawRect(
//            Color.Transparent,
//            topLeft = Offset(start, 0f),
//            size = Size(size, height = imageHeight),
//            blendMode = BlendMode.SrcIn
//        )
//    }
//}
//
//private fun DrawScope.drawWithLayer(block: DrawScope.() -> Unit) {
//    with(drawContext.canvas.nativeCanvas) {
//        val checkPoint = saveLayer(null, null)
//        block()
//        restoreToCount(checkPoint)
//    }
//}