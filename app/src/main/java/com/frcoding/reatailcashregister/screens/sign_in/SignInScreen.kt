package com.frcoding.reatailcashregister.screens.sign_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.frcoding.reatailcashregister.R

@Composable
fun SignInScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val username = viewModel.username.collectAsState()
    val password = viewModel.password.collectAsState()
    val loginResult = viewModel.loginResult.collectAsState()

    Column (
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            value = username.value,
            onValueChange = { viewModel.updateUsername(it) },
            label = { Text(text = "Username") },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Username") }
        )

        OutlinedTextField(
            value = password.value,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text(text = "Password") },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp))

        Button(
            onClick = {
                viewModel.onSignInClick()
//                loginResult?.let {
//                    //Navigacija na glavni ekran
//                    navController.navigate("main_screen")
//                }
                      },
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
        ) {
            Text(
                text = stringResource(id = R.string.sign_in),
                fontSize = 16.sp,
                modifier = Modifier.padding(0.dp, 6.dp)
            )
        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp))

        TextButton(
            onClick = {
            //preusmeravanje na sign up stranicu
                navController.navigate("sign_up_screen")
            }
        ) {
            Text(text = stringResource(id = R.string.sign_up_description), fontSize = 16.sp)
        }
    }

    LaunchedEffect(loginResult.value) {
        loginResult.value?.let { user ->
            // Check if user is not null (meaning login was successful)
            if (user != null) {
                navController.navigate("main_screen") {
                    // Clear the back stack to avoid returning to the login screen
                    popUpTo("sign_in_screen") { inclusive = true }
                }
            }
        }
    }

}