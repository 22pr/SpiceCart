package com.example.spicecart

import android.os.Bundle
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spicecart.ui.theme.SpiceCartTheme

class SignupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpiceCartTheme {
                val navController = rememberNavController()
                SignupScreen(navController)
            }
        }
    }
}

@Composable
fun SignupScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var fullNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val isSignupEnabled = listOf(fullNameError, emailError, passwordError, confirmPasswordError).all { it == null } &&
            fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5E1C8)), // Beige Background
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

            Text(
                text = "Create an Account",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D4037) // Deep Brown
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Full Name Field
            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    fullNameError = if (it.isEmpty()) "Full name cannot be empty" else null
                },
                label = { Text("Full Name") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Full Name Icon") },
                singleLine = true,
                isError = fullNameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            fullNameError?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = when {
                        it.isEmpty() -> "Email cannot be empty"
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() -> "Invalid email format"
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
                Text(text = it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = when {
                        it.isEmpty() -> "Password cannot be empty"
                        it.length < 6 -> "Password must be at least 6 characters"
                        else -> null
                    }
                },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    Icon(icon, contentDescription = "Toggle Password", modifier = Modifier.clickable { passwordVisible = !passwordVisible })
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordError != null,
                modifier = Modifier.fillMaxWidth()
            )
            passwordError?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = when {
                        it.isEmpty() -> "Confirm password cannot be empty"
                        it != password -> "Passwords do not match"
                        else -> null
                    }
                },
                label = { Text("Confirm Password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Confirm Password Icon") },
                singleLine = true,
                isError = confirmPasswordError != null,
                modifier = Modifier.fillMaxWidth()
            )
            confirmPasswordError?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Signup Button
            Button(
                onClick = { /* Perform Signup Logic */ },
                enabled = isSignupEnabled, // Button only enabled if all inputs are valid
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)), // Golden Yellow
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Already have an account? Login
            Row {
                Text(text = "Already have an account?", fontSize = 14.sp, color = Color(0xFF5D4037))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Login",
                    fontSize = 14.sp,
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SpiceCartTheme {
        SignupScreen(rememberNavController())
    }
}
