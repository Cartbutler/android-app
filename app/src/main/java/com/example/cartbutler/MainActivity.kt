package com.example.cartbutler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cartbutler.components.AppFooter
import com.example.cartbutler.screens.HomePage
import com.example.cartbutler.screens.CartScreen
import com.example.cartbutler.screens.ProductScreen
import com.example.cartbutler.screens.ProfileScreen
import com.example.cartbutler.ui.theme.CartbutlerTheme
import com.example.cartbutler.screens.*

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CartbutlerTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "home"

                Scaffold(
                    bottomBar = {
                        AppFooter(navController = navController, currentRoute = currentRoute)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            HomePage(navController = navController)
                        }
                        composable("cart") {
                            CartScreen()
                        }
                        composable("profile") {
                            ProfileScreen()
                        }
                        composable("search/{query}") { backStackEntry ->
                            val query = backStackEntry.arguments?.getString("query") ?: ""
                            ProductSearchScreen(navController = navController, searchQuery = query)
                        }
                        composable("product/{productName}") { backStackEntry ->
                            val productName = backStackEntry.arguments?.getString("productName") ?: ""
                            ProductScreen(
                                productName = productName,
                                navController = navController
                            )
                        }
                        composable("product/{productId}") { backStackEntry ->
                            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                            ProductDetailScreen(
                                navController = navController,
                                productId = productId
                            )
                        }
                        composable("productDetail/{productId}") { backStackEntry ->
                            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                            ProductDetailScreen(navController, productId)
                        }
                    }
                }
            }
        }
    }
}
