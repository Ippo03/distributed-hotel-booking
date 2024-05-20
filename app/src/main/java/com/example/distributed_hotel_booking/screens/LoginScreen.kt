package com.example.distributed_hotel_booking.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
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

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val loginButtonFocusRequester = remember { FocusRequester() }

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
                TextFieldUsername(
                    viewModel,
                    modifier = Modifier
                        .focusRequester(emailFocusRequester)
                        .onKeyEvent {
                            if (it.key == Key.Enter) {
                                passwordFocusRequester.requestFocus()
                                true
                            } else false
                        }
                )
                TextFieldPassword(
                    viewModel,
                    modifier = Modifier
                        .focusRequester(passwordFocusRequester)
                        .onKeyEvent {
                            if (it.key == Key.Enter) {
                                loginButtonFocusRequester.requestFocus()
                                true
                            } else false
                        }
                )
                Button(
                    onClick = {
                        viewModel.onLogin(navController, sharedViewModel)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                        .width(140.dp)
                        .focusable(true)
                        .focusRequester(loginButtonFocusRequester)
                ) {
                    Text(
                        text = "Login",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TextFieldUsername(viewModel: LoginViewModel, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = viewModel.usernameText.value,
        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "usernameIcon") },
        onValueChange = { viewModel.usernameText.value = it },
        label = { Text(text = "Username") },
        placeholder = { Text(text = "Enter your username") },
        singleLine = true,
        modifier = modifier
    )
}

@Composable
fun TextFieldPassword(viewModel:LoginViewModel, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = viewModel.passwordText.value,
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "passwordIcon") },
        onValueChange = { viewModel.passwordText.value = it },
        label = { Text(text = "Password") },
        placeholder = { Text(text = "Enter your password") },
        singleLine = true,
        modifier = modifier
    )
}

//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    LoginScreen(navController = rememberNavController(), viewModel)
//}

