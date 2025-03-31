package com.example.spicecart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CartScreen(navController: NavController) {
    val deliveryCharge = 2.50
    val subtotal = cartItems.sumOf { it.dish.price * it.quantity }
    val total = subtotal + deliveryCharge

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF5E1C8))
    ) {
        Text(
            text = "Your Cart",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF5D4037)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Text(
                text = "Your cart is empty.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(cartItems) { item ->
                    CartItemRow(item)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.Gray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            SummaryRow("Subtotal", "£${"%.2f".format(subtotal)}")
            SummaryRow("Delivery", "£${"%.2f".format(deliveryCharge)}")
            SummaryRow("Total", "£${"%.2f".format(total)}", isBold = true)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("payment") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Proceed to Checkout", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.dish.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF5D4037)
            )
            Text(
                text = "£${"%.2f".format(item.dish.price)} x ${item.quantity}",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = Color(0xFF5D4037)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = Color(0xFF5D4037)
        )
    }
}
