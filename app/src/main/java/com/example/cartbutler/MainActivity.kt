package com.example.cartbutler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cartbutler.screens.HomePage
import com.example.cartbutler.screens.SearchScreen
import com.example.cartbutler.ui.theme.CartbutlerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CartbutlerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomePage(navController = navController)
                    }
                    composable("search/{product}") { backStackEntry ->
                        val product = backStackEntry.arguments?.getString("product") ?: ""
                        SearchScreen(product = product)
                    }
                }
            }
        }
    }
}
