package com.example.spicecart.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spicecart.R

data class Dish(val name: String, val image: Int, val price: Double, val category: String)

@Composable
fun HomeScreen(navController: NavController) {
    var menuExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Biryani", "South Indian", "Snacks", "Sweets", "Tandoori")

    val allDishes = listOf(
        Dish("Hyderabadi Biryani", R.drawable.biryani, 6.50, "Biryani"),
        Dish("Paneer Butter Masala", R.drawable.paneer, 5.99, "Tandoori"),
        Dish("Idli & Chutney", R.drawable.idli, 3.00, "South Indian"),
        Dish("Kachori", R.drawable.kachori, 2.50, "Snacks"),
        Dish("Rasgulla", R.drawable.rasgulla, 2.20, "Sweets"),
        Dish("Tandoori Chicken", R.drawable.tandoori_chicken, 7.20, "Tandoori")
    )

    val filteredDishes = if (selectedCategory == "All") allDishes else allDishes.filter { it.category == selectedCategory }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5E1C8))
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    DropdownMenuItem(text = { Text("Settings") }, onClick = { menuExpanded = false })
                    DropdownMenuItem(text = { Text("About") }, onClick = { menuExpanded = false })
                    DropdownMenuItem(text = { Text("Privacy Policy") }, onClick = { menuExpanded = false })
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome to SpiceCart!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D4037)
        )

        Text(
            text = "Authentic Indian food delivered to your doorstep",
            fontSize = 16.sp,
            color = Color(0xFF8D6E63)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Category Filters
        Text(
            text = "Filter by Category",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF5D4037)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Dishes",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF5D4037)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(filteredDishes) { dish ->
                DishCard(dish)
            }
        }
    }
}

@Composable
fun DishCard(dish: Dish) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp,
        color = Color.White,
        modifier = Modifier
            .width(220.dp)
            .height(180.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = dish.image),
                contentDescription = dish.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = dish.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D4037),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = "£${"%.2f".format(dish.price)}", // 💷 Price in Pounds
                fontSize = 14.sp,
                color = Color(0xFF8D6E63),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* Add to cart logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .height(36.dp)
            ) {
                Text("Add to Cart", color = Color.Black, fontSize = 14.sp)
            }
        }
    }
}
