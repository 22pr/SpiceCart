package com.example.spicecart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.spicecart.ui.theme.SpiceCartTheme
import kotlinx.coroutines.delay
import com.example.spicecart.ui.screens.*
import com.example.spicecart.ui.screens.ProfileScreen




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpiceCartTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

// Root Navigation for Splash/Login/Signup -> Bottom Navigation
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("main") { BottomNavigationContainer() }
        composable("profile") { ProfileScreen() }

    }
}

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2500)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5E1C8)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.spicecart_logo),
                contentDescription = "SpiceCart Logo",
                modifier = Modifier.size(180.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "SpiceCart",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D4037),
                modifier = Modifier.alpha(1f)
            )
        }
    }
}


@Composable
fun BottomNavigationContainer() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem("Home", "home", Icons.Default.Home),
        BottomNavItem("My Orders", "orders", Icons.Default.List),
        BottomNavItem("Cart", "cart", Icons.Default.ShoppingCart),
        BottomNavItem("Payment", "payment", Icons.Default.AccountBalanceWallet)

    )

    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController) }

            composable("orders") { OrdersScreen() }
            composable("cart") { CartScreen() }
            composable("payment") { PaymentScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

