package com.example.spicecart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Order(
    val items: List<CartItem> = emptyList(),
    val status: String = "",
    val eta: String = ""
)

val orderHistory = mutableStateListOf<Order>()

@Composable
fun OrdersScreen(
    rootNavController: NavController,
    setTab: (String) -> Unit
) {
    val firestore = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUserEmail = auth.currentUser?.email

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(currentUserEmail) {
        if (currentUserEmail != null) {
            try {
                val snapshot = firestore.collection("users")
                    .document(currentUserEmail)
                    .collection("orders")
                    .get()
                    .await()

                val fetchedOrders = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Order::class.java)
                }
                orderHistory.clear()
                orderHistory.addAll(fetchedOrders)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5E1C8))
            .padding(16.dp)
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
        IconButton(
            onClick = { setTab("home") }
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF5D4037))
        }

        Text(
            text = "My Orders",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF5D4037)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(color = Color(0xFF5D4037))
        } else if (orderHistory.isEmpty()) {
            Text(
                text = "You haven’t placed any orders yet.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        } else {
            val latestOrder = orderHistory.last()

            Text(
                text = "Current Order",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF5D4037)
            )

            Spacer(modifier = Modifier.height(8.dp))
            OrderCard(order = latestOrder)

            Spacer(modifier = Modifier.height(24.dp))
            Divider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Order History",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color(0xFF5D4037)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(orderHistory.dropLast(1).reversed()) { order ->
                    OrderCard(order = order)
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            order.items.forEach {
                Text(
                    text = "${it.dish.name} x${it.quantity} - £${"%.2f".format(it.dish.price * it.quantity)}",
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Status: ${order.status}",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "ETA: ${order.eta}",
                color = Color(0xFF8D6E63),
                fontSize = 13.sp
            )
        }
    }
}
