package com.example.cartbutler.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cartbutler.network.Product
import com.example.cartbutler.viewmodel.ProductDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Int?
) {
    val viewModel: ProductDetailViewModel = viewModel()
    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage: String? by viewModel.errorMessage.collectAsState()
    val errorText = errorMessage

    LaunchedEffect(productId) {
        productId?.let { viewModel.loadProduct(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.productName ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
                isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                errorText != null -> Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
                product != null -> ProductDetailsContent(product!!)
            }
        }
    }
}

@Composable
private fun ProductDetailsContent(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AsyncImage(
            model = product.imagePath,
            contentDescription = product.productName,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = product.productName,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "$${product.price}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { /* TODO: CREATE ADDTOCART FUNCTION */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Add to cart")
        }
    }
}
