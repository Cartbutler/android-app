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
import com.example.cartbutler.screens.SearchScreen
import com.example.cartbutler.ui.screens.ProfileScreen
import com.example.cartbutler.ui.theme.CartbutlerTheme

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
                            CartScreen(navController = navController)
                        }
                        composable("profile") {
                            ProfileScreen(navController = navController)
                        }
                        composable("search/{product}") { backStackEntry ->
                            val product = backStackEntry.arguments?.getString("product") ?: ""
                            SearchScreen(product = product, navController = navController)
                        }
                    }
                }
            }
        }
    }
}
