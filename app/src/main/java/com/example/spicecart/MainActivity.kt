package com.example.spicecart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spicecart.ui.theme.SpiceCartTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpiceCartTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") { SplashScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("signup") { SignupScreen(navController) }

                //composable("home") { MainScreen() }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2500) // Show splash for 2.5 seconds
        navController.navigate("login")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5E1C8)), // Warm Beige Background
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.spicecart_logo),
                contentDescription = "SpiceCart Logo",
                modifier = Modifier.size(180.dp) // Increased Logo Size
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "SpiceCart",
                fontSize = 40.sp, // Even Larger Font Size
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D4037), // Deep Brown Text
                modifier = Modifier.alpha(1f)
            )
        }
    }
}
