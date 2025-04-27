package com.example.spicecart.ui.screens

import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController) {
    val deliveryCharge = 2.50
    val subtotal = cartItems.sumOf { it.dish.price * it.quantity }
    val total = subtotal + deliveryCharge

    var cardName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    var suggestions by remember { mutableStateOf(listOf<AutocompletePrediction>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var paymentSuccess by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUserEmail = auth.currentUser?.email

    // Initialize Places API
    val placesClient = remember {
        if (!Places.isInitialized()) {
            Places.initialize(context.applicationContext, "AIzaSyAHfUTGjLSl6WHruPm0j8n7TAsAWoVFiZk")
        }
        Places.createClient(context)
    }
    val sessionToken = remember { AutocompleteSessionToken.newInstance() }

    // Fetch address from Firestore on screen load
    LaunchedEffect(currentUserEmail) {
        if (currentUserEmail != null) {
            try {
                val document = firestore.collection("users")
                    .document(currentUserEmail)
                    .get()
                    .await()
                if (document.exists()) {
                    address = document.getString("address") ?: ""
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF5D4037))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Payment",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF5D4037)
            )

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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = expiry,
                    onValueChange = { expiry = it },
                    label = { Text("Expiry (MM/YY)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    label = { Text("CVV") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { query ->
                    address = query
                    if (query.length >= 3) {
                        val request = FindAutocompletePredictionsRequest.builder()
                            .setSessionToken(sessionToken)
                            .setQuery(query)
                            .build()

                        placesClient.findAutocompletePredictions(request)
                            .addOnSuccessListener { response ->
                                suggestions = response.autocompletePredictions
                            }
                            .addOnFailureListener {
                                suggestions = emptyList()
                            }
                    } else {
                        suggestions = emptyList()
                    }
                },
                label = { Text("Delivery Address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Show suggestions
            suggestions.forEach { prediction ->
                Text(
                    text = prediction.getFullText(null).toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            address = prediction.getFullText(null).toString()
                            suggestions = emptyList()
                        }
                        .padding(8.dp),
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let {
                Text(text = it, color = Color.Red, fontSize = 14.sp)
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
                    if (cardName.isBlank() || cardNumber.length !in 12..19 ||
                        expiry.length < 5 || cvv.length != 3 || address.isBlank()
                    ) {
                        errorMessage = "Please enter all valid card and address details"
                    } else {
                        errorMessage = null
                        paymentSuccess = true

                        val order = Order(
                            items = cartItems.map { it.copy() },
                            status = "Accepted",
                            eta = "Delivery in 30 mins"
                        )

                        orderHistory.add(order)

                        if (currentUserEmail != null) {
                            firestore.collection("users")
                                .document(currentUserEmail)
                                .collection("orders")
                                .add(order)
                        }

                        cartItems.clear()

                        scope.launch {
                            snackbarHostState.showSnackbar("Payment successful! Thank you 🎉")
                            delay(1000)
                            navController.navigate("orders")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
            ) {
                Text(
                    text = "Pay £${"%.2f".format(total)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
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
