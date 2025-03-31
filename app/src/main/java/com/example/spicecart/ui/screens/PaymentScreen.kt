package com.example.spicecart.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun PaymentScreen() {
    val deliveryCharge = 2.50
    val subtotal = cartItems.sumOf { it.dish.price * it.quantity }
    val total = subtotal + deliveryCharge

    var cardName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var paymentSuccess by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5E1C8))
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text("Payment", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF5D4037))

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = cardName,
                onValueChange = { cardName = it },
                label = { Text("Cardholder Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = { Text("Card Number") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = expiry,
                    onValueChange = { expiry = it },
                    label = { Text("Expiry (MM/YY)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    label = { Text("CVV") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Delivery Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = "Total to Pay: £${"%.2f".format(total)}",
                fontSize = 18.sp,
                color = Color(0xFF5D4037),
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Basic Validations
                    if (cardName.isBlank() || cardNumber.length !in 12..19 ||
                        expiry.length < 5 || cvv.length != 3 || address.isBlank()
                    ) {
                        errorMessage = "Please enter all valid card and address details"
                    } else {
                        errorMessage = null
                        paymentSuccess = true
                        cartItems.clear()
                        scope.launch {
                            snackbarHostState.showSnackbar("Payment successful! Thank you 🎉")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
            ) {
                Text("Pay £${"%.2f".format(total)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }

            if (paymentSuccess) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Payment completed successfully!",
                    fontSize = 16.sp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
