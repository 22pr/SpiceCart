package com.example.spicecart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavController,
    email: String,
    backStackEntry: NavBackStackEntry? = null
) {
    val nameState = remember { mutableStateOf("Prathima") }
    val addressState = remember { mutableStateOf("") }

    val actualEmail = backStackEntry?.arguments?.getString("email") ?: email

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5E1C8))
                .padding(padding)
                .padding(16.dp)
        ) {
            // ✅ Fixed Back Button Navigation
            IconButton(onClick = {
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                    launchSingleTop = true
                }
            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF5D4037))
            }

            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF5D4037)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = actualEmail,
                onValueChange = {},
                readOnly = true,
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.DarkGray,
                    disabledBorderColor = Color.Gray,
                    disabledLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = addressState.value,
                onValueChange = { addressState.value = it },
                label = { Text("Address") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Profile updated successfully!")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
            ) {
                Text("Update Details", fontSize = 16.sp, color = Color.Black)
            }
        }
    }
}
