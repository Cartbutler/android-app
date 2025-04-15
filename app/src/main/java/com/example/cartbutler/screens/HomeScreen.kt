package com.example.cartbutler.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cartbutler.components.CategorySection
import com.example.cartbutler.components.SearchWithDropdown
import com.example.cartbutler.viewmodel.CategoryViewModel
import com.example.cartbutler.viewmodel.ProductSuggestionViewModel

@ExperimentalMaterial3Api
@Composable
fun HomePage(navController: NavController) {
    val context = LocalContext.current
    val categoryViewModel: CategoryViewModel = viewModel()
    val categories by categoryViewModel.categories.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val productSuggestionViewModel: ProductSuggestionViewModel = viewModel()
    val suggestions by productSuggestionViewModel.suggestions.collectAsState()

    LaunchedEffect(Unit) {
        categoryViewModel.loadCategories(context)
    }

    LaunchedEffect(context.resources.configuration) {
        categoryViewModel.refresh(context)
    }

    LaunchedEffect(searchQuery) {
        productSuggestionViewModel.fetchSuggestions(searchQuery)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                SearchWithDropdown(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    filteredProducts = suggestions.map { it.name },
                    onSuggestionSelected = { query ->
                        searchQuery = query
                        navController.navigate("search/$query")
                    },
                    onSearchConfirmed = { query ->
                        if (query.isNotBlank()) {
                            navController.navigate("search/$query")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CategorySection(
                    categories = categories,
                    onCategoryClick = { category ->
                        navController.navigate("categoryProducts/${category.categoryId}/${category.categoryName}")
                    }
                )
            }
        }
    )
}