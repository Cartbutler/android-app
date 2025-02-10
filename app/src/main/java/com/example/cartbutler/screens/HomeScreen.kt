package com.example.cartbutler.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cartbutler.components.CategorySection
import com.example.cartbutler.components.SearchWithDropdown
import com.example.cartbutler.viewmodel.CategoryViewModel

@ExperimentalMaterial3Api
@Composable
fun HomePage(navController: NavController) {
    val categoryViewModel: CategoryViewModel = viewModel()
    val categories = categoryViewModel.categories

    var searchQuery by remember { mutableStateOf("") }
    val allProducts = listOf("Apple", "Banana", "Carrot", "Milk", "Bread", "Cheese", "Coca-Cola", "Pizza")
    val filteredProducts = remember(searchQuery) {
        if (searchQuery.isEmpty()) emptyList()
        else allProducts.filter { it.contains(searchQuery, ignoreCase = true) }.sorted()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                SearchWithDropdown(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    filteredProducts = filteredProducts,
                    onProductSelected = { product ->
                        navController.navigate("search/$product")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CategorySection(
                    categories = categories,
                    onCategoryClick = { category ->
                        println("Navigating to category: ${category.category_name}")
                        navController.navigate("category/${category.category_id}")
                    }
                )
            }
        }
    )
}
