package com.example.spicecart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spicecart.ui.theme.SpiceCartTheme


class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpiceCartTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5E1C8))
            .padding(16.dp)
    ) {
        // Welcome Text
        Text(
            text = "Welcome to SpiceCart!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D4037),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Subtitle
        Text(
            text = "Authentic Indian food delivered to you",
            fontSize = 16.sp,
            color = Color(0xFF8D6E63)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Food Categories
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
