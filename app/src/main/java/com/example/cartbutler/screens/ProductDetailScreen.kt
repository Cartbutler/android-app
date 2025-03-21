package com.example.cartbutler.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            product?.let {
                ProductDetailsContent(
                    product = it,
                    onAddToCart = {
                        scope.launch {
                            productId?.let { id -> cartViewModel.incrementQuantity(id) }
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
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
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
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(Modifier.height(8.dp))

        if (product.minPrice != null && product.maxPrice != null && product.minPrice < product.maxPrice) {
            Text(
                text = "${formatCurrency(product.minPrice)} - ${formatCurrency(product.maxPrice)}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Text(
                text = formatCurrency(product.price),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge
        )

        product.stores?.takeIf { it.isNotEmpty() }?.let { stores ->
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Price in stores:",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.height(8.dp))

            stores.forEach { store ->
                StorePriceItem(
                    storeName = store.storeName,
                    price = store.price?.toFloatOrNull() ?: 0f
                )
            }
        }

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

@Composable
private fun StorePriceItem(storeName: String, price: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = storeName,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = formatCurrency(price),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}