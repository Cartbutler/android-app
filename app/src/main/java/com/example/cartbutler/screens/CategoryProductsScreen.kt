package com.example.cartbutler.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import com.example.cartbutler.components.ProductItem
import com.example.cartbutler.components.formatCurrency
import com.example.cartbutler.viewmodel.CategoryProductsViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp

@Composable
fun CategoryProductsScreen(
    navController: NavController,
    categoryId: Int
) {
    val viewModel: CategoryProductsViewModel = viewModel()

    LaunchedEffect(key1 = categoryId) {
        viewModel.loadProductsByCategory(categoryId)
    }

    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            errorMessage != null -> {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(products) { product ->
                        Column {
                            product.categoryName?.let { name ->
                                Text(text = name)
                            }
                            ProductItem(
                                productName = product.productName,
                                price = formatCurrency(product.price),
                                imageUrl = product.imagePath,
                                onClick = {
                                    navController.navigate("productDetail/${product.productId}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}