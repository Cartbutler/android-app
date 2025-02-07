package com.example.cartbutler.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cartbutler.ui.components.CategorySection
import com.example.cartbutler.ui.components.SearchWithDropdown

@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun HomePage() {
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
                    onProductSelected = { searchQuery = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CategorySection()
            }
        }
    )
}
