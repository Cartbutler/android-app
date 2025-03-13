package com.example.cartbutler.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cartbutler.R
import com.example.cartbutler.components.formatCurrency
import com.example.cartbutler.network.networkModels.Product
import com.example.cartbutler.viewmodel.CartViewModel
import com.example.cartbutler.viewmodel.ProductDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Int?,
    cartViewModel: CartViewModel
) {
    val productDetailViewModel: ProductDetailViewModel = viewModel()
    val product by productDetailViewModel.product.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(productId) {
        productId?.let { productDetailViewModel.loadProduct(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.productName ?: stringResource(R.string.Details)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            when {
                product != null -> ProductDetailsContent(
                    product = product!!,
                    onAddToCart = {
                        scope.launch {
                            productId?.let { cartViewModel.addToCart(it) }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ProductDetailsContent(
    product: Product,
    onAddToCart: () -> Unit
) {
    Column(Modifier.padding(16.dp)) {
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
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = formatCurrency(product.price),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onAddToCart,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = stringResource(R.string.cart_icon_desc),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.add_to_cart))
            }
        }
    }
}