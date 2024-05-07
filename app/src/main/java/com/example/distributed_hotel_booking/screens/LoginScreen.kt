package com.example.distributed_hotel_booking.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.distributed_hotel_booking.R
import com.example.distributed_hotel_booking.viewmodel.LoginViewModel
import com.example.distributed_hotel_booking.viewmodel.SharedViewModel

@Composable
fun LoginScreen(navController: NavController, sharedViewModel : SharedViewModel) {
    val viewModel: LoginViewModel = viewModel()

    // Temp object to show that the HomeScreen is working
    Surface(color = Color.White, modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
        val expanded = remember { mutableStateOf(false) }
        val extraPadding = if (expanded.value) 48.dp else 0.dp

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            // Display a welcome message
            Row {
                Text(
                    text = "Welcome to Hotel Booking !",
                    color = Color.Black,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
            // Show the app logo
            Row(
                modifier = Modifier
                    .padding(120.dp)
                    .align(Alignment.CenterHorizontally)
            ){
                Image(
                    painter = painterResource(id = R.drawable.booking_logo),
                    contentDescription = "app logo"
                )
            }
            // Dummy input fields for email and password
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                TextFieldEmail()
                TextFieldPassword()
                Button(
                    onClick = {
                        viewModel.onLogin(navController, sharedViewModel)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                        .width(140.dp)
                ) {
                    Text(
                        text = "Login",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
            }
//            Row (
//                modifier = Modifier
//                    .padding(120.dp)
//                    .align(Alignment.CenterHorizontally)
//            ){
//                ElevatedButton(
//                    enabled = true,
//                    onClick = { expanded.value = !expanded.value },
//                    modifier = Modifier.wrapContentSize((Alignment.Center))
//                ) {
//                    Text(text = "Enter as User")
//                    if (expanded.value) {
//                        Column {
//                            TextFieldEmail()
//                            TextFieldPassword()
//                            Button(
//                                onClick = { navController.navigate(Screen.UserHomeScreen.route) },
//                                modifier = Modifier.wrapContentSize((Alignment.Center))
//                            )
//                            {
//                                Text(
//                                    text = "Login",
//                                    modifier = Modifier.wrapContentSize((Alignment.Center))
//                                )
//                            }
//                        }
//                    }
//                }
//
//            }
//            // Dummy button to navigate to the user home screen
//            Button(
//                onClick = { navController.navigate(Screen.UserHomeScreen.route) },
////                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
//            ) {
//                Text(text = "Go to User Home Screen")
//            }
    }
}
}

@Composable
fun TextFieldEmail() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    return OutlinedTextField(
        value = text,
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "emailIcon") },
        onValueChange = {
            text = it
        },
        label = { Text(text = "Email address") },
        placeholder = { Text(text = "Enter your e-mail") },
    )
}

@Composable
fun TextFieldPassword() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    return OutlinedTextField(
        value = text,
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "passwordIcon") },
        onValueChange = {
            text = it
        },
        label = { Text(text = "Password") },
        placeholder = { Text(text = "Enter your password") },
    )
}

//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    LoginScreen(navController = rememberNavController(), viewModel)
//}


// This is the home screen of the app - LOGIN
// It should contain two buttons "Enter as User" and "Enter as Manager"