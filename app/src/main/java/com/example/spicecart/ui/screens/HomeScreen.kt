package com.example.spicecart.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import kotlinx.coroutines.launch

// Shared cart
val cartItems = mutableStateListOf<CartItem>()

data class Dish(val name: String, val image: Int, val price: Double, val category: String)
data class CartItem(val dish: Dish, var quantity: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var menuExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedDish by remember { mutableStateOf<Dish?>(null) }
    var quantity by remember { mutableStateOf(1) }

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

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

    selectedDish?.let { dish ->
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch { sheetState.hide() }
                selectedDish = null
                quantity = 1
            },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add ${dish.name}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { if (quantity > 1) quantity-- }) { Text("-") }
                    Text(text = "$quantity", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp))
                    Button(onClick = { quantity++ }) { Text("+") }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val existing = cartItems.find { it.dish.name == dish.name }
                        if (existing != null) existing.quantity += quantity
                        else cartItems.add(CartItem(dish, quantity))

                        coroutineScope.launch {
                            sheetState.hide()
                            snackbarHostState.showSnackbar("${dish.name} x$quantity added to cart")
                        }

                        selectedDish = null
                        quantity = 1
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                ) {
                    Text("Add £${"%.2f".format(dish.price * quantity)} to Cart", color = Color.Black)
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5E1C8))
                .padding(padding)
                .padding(16.dp)
        ) {
            // Top bar with dropdown
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Box {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, shape = CircleShape)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "User Menu", tint = Color(0xFF5D4037))
                    }

                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(text = { Text("Profile") }, onClick = {
                            navController.navigate("profile")
                            menuExpanded = false
                        })
                        DropdownMenuItem(text = { Text("Settings") }, onClick = { menuExpanded = false })
                        DropdownMenuItem(text = { Text("About") }, onClick = { menuExpanded = false })
                        DropdownMenuItem(text = { Text("Privacy Policy") }, onClick = { menuExpanded = false })
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Welcome to SpiceCart!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5D4037))
            Text("Authentic Indian food delivered to your doorstep", fontSize = 16.sp, color = Color(0xFF8D6E63))
            Spacer(modifier = Modifier.height(24.dp))

            Text("Filter by Category", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF5D4037))
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
            Text("Dishes", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF5D4037))
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredDishes) { dish ->
                    DishCard(dish = dish) {
                        selectedDish = it
                        coroutineScope.launch { sheetState.show() }
                    }
                }
            }
        }
    }
}

@Composable
fun DishCard(dish: Dish, onAddClick: (Dish) -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
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
                dish.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D4037),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                "£${"%.2f".format(dish.price)}",
                fontSize = 14.sp,
                color = Color(0xFF8D6E63),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onAddClick(dish) },
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
