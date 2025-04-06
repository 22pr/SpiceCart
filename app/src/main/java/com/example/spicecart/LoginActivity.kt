package com.example.spicecart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spicecart.ui.theme.SpiceCartTheme
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpiceCartTheme {
                val navController = rememberNavController()
                LoginScreen(navController)
            }
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val isLoginEnabled = emailError == null && passwordError == null && email.isNotEmpty() && password.isNotEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5E1C8)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.spicecart_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Welcome Back!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5D4037))
            Spacer(modifier = Modifier.height(8.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = when {
                        it.isBlank() -> "Email cannot be empty"
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() -> "Invalid email"
                        else -> null
                    }
                },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                singleLine = true,
                isError = emailError != null,
                modifier = Modifier.fillMaxWidth()
            )

            emailError?.let {
                Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(top = 4.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = when {
                        it.isBlank() -> "Password cannot be empty"
                        it.length < 6 -> "Minimum 6 characters"
                        else -> null
                    }
                },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    Icon(
                        image,
                        contentDescription = "Toggle Password",
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordError != null,
                modifier = Modifier.fillMaxWidth()
            )

            passwordError?.let {
                Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start).padding(top = 4.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Forgot Password?",
                fontSize = 14.sp,
                color = Color(0xFF5D4037),
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { /* TODO */ }
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoading = true
                    auth.signInWithEmailAndPassword(email.trim(), password.trim())
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                navController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                },
                enabled = isLoginEnabled && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text("Don't have an account?", fontSize = 14.sp, color = Color(0xFF5D4037))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sign Up",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        navController.navigate("signup")
                    }
                )
            }
        }
    }
}
