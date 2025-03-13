package com.example.cartbutler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cartbutler.components.AppFooter
import com.example.cartbutler.network.SessionManager
import com.example.cartbutler.repositories.CartRepository
import com.example.cartbutler.screens.CartScreen
import com.example.cartbutler.screens.HomePage
import com.example.cartbutler.screens.ProductDetailScreen
import com.example.cartbutler.screens.ProductSearchScreen
import com.example.cartbutler.screens.ProfileScreen
import com.example.cartbutler.ui.theme.CartbutlerTheme
import com.example.cartbutler.viewmodel.CartViewModel
import com.example.cartbutler.network.RetrofitInstance
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CartbutlerTheme {
                val navController = rememberNavController()
                val sessionManager = SessionManager(this@MainActivity)
                val cartRepository = CartRepository(RetrofitInstance.api, sessionManager)
                val cartViewModel = remember { CartViewModel(cartRepository) }

                Scaffold(
                    bottomBar = {
                        AppFooter(
                            navController = navController,
                            cartViewModel = cartViewModel
                        )
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
                            ProductSearchScreen(
                                navController = navController,
                                searchQuery = query,
                                categoryId = null,
                                passedCategoryName = null
                            )
                        }
                        composable("categoryProducts/{categoryId}/{categoryName}") { backStackEntry ->
                            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
                            val categoryName = backStackEntry.arguments?.getString("categoryName")
                            ProductSearchScreen(
                                navController = navController,
                                searchQuery = null,
                                categoryId = categoryId,
                                passedCategoryName = categoryName
                            )
                        }
                        composable("productDetail/{productId}") { backStackEntry ->
                            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()

                            ProductDetailScreen(
                                navController = navController,
                                productId = productId,
                                cartViewModel = cartViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
