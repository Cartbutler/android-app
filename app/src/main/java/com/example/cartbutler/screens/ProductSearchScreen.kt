package com.example.cartbutler.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cartbutler.R
import com.example.cartbutler.components.ProductItem
import com.example.cartbutler.components.formatCurrency
import com.example.cartbutler.viewmodel.ProductSearchViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.material.icons.Icons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSearchScreen(
    navController: NavController,
    searchQuery: String?,
    categoryId: Int?,
    passedCategoryName: String?
) {
    val viewModel: ProductSearchViewModel = viewModel(
        key = "search_${searchQuery}_category_${categoryId}"
    )

    LaunchedEffect(key1 = searchQuery, key2 = categoryId) {
        when {
            searchQuery != null -> viewModel.loadProductsByQuery(searchQuery)
            categoryId != null && passedCategoryName != null ->
                viewModel.loadProductsByCategory(categoryId, passedCategoryName)
        }
    }

    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val currentSearch by viewModel.searchQuery.collectAsState()
    val categoryName by viewModel.categoryName.collectAsState()

    val title: String = when {
        currentSearch != null -> stringResource(R.string.searchingFor, currentSearch ?: "")
        categoryName != null -> categoryName!!
        else -> stringResource(R.string.products_title)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                errorMessage != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(
                            onClick = { viewModel.retry() },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
                products.isEmpty() -> {
                    Text(
                        text = when {
                            currentSearch != null -> stringResource(
                                R.string.no_results_search,
                                currentSearch ?: ""
                            )
                            categoryName != null -> stringResource(
                                R.string.no_results_category,
                                categoryName ?: ""
                            )
                            else -> stringResource(R.string.no_products)
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(products) { product ->
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