package com.example.cartbutler.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

/**
 * Displays the bottom navigation bar with three items: Home, Cart, and Profile.
 *
 * @param navController The [NavController] used to handle navigation between destinations.
 * @param currentRoute The current route to determine which navigation item is selected.
 */
@Composable
fun AppFooter(navController: NavController, currentRoute: String) {
    val items = listOf(
        BottomNavItem("home", "Home", Icons.Filled.Home),
        BottomNavItem("cart", "Cart", Icons.Filled.ShoppingCart),
        BottomNavItem("profile", "Profile", Icons.Filled.Person)
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.label)
                },
                label = { Text(text = item.label) }
            )
        }
    }
}
