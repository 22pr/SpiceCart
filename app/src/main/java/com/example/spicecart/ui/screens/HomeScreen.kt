package com.example.spicecart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController


@Composable
fun HomeScreen(navController: NavController)
 {
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5E1C8))
            .padding(16.dp)
    ) {
        // Top App Bar Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Box {
                IconButton(
                    onClick = { menuExpanded = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Menu",
                        tint = Color(0xFF5D4037)
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Profile") },
                        onClick = {
                            navController.navigate("profile")
                            menuExpanded = false
                        }

                    )
                    DropdownMenuItem(
                        text = { Text("Settings") },
                        onClick = { /* Navigate to Settings */ menuExpanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("About") },
                        onClick = { /* Navigate to About */ menuExpanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Privacy Policy") },
                        onClick = { /* Navigate to Privacy Policy */ menuExpanded = false }
                    )
                }
            }
        }

        // Welcome Text
        Text(
            text = "Welcome to SpiceCart!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D4037),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "Authentic Indian food delivered to you",
            fontSize = 16.sp,
            color = Color(0xFF8D6E63)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Popular Categories",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF5D4037)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(5) { index ->
                CategoryCard(
                    title = when (index) {
                        0 -> "Biryani"
                        1 -> "South Indian"
                        2 -> "Snacks"
                        3 -> "Sweets"
                        else -> "Tandoori"
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(title: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = Modifier
            .width(140.dp)
            .height(100.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF5D4037)
            )
        }
    }
}
