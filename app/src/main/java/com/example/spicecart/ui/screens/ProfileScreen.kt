package com.example.spicecart.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {
    var name by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("johndoe@example.com") }
    var address by remember { mutableStateOf("123, Spice Street, Hyderabad") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Your Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF5D4037)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Delivery Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                // In future, this will update Firebase or local DB
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Profile", color = Color.Black, fontSize = 16.sp)
        }
    }
}
