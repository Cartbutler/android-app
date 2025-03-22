package com.example.cartbutler.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.cartbutler.R
import com.example.cartbutler.network.networkModels.StoreWithTotals
import com.example.cartbutler.viewmodel.CartViewModel

@Composable
fun StoreResultsScreen(cartViewModel: CartViewModel, navController: NavController) {
    val storeResults = cartViewModel.storeResults.collectAsState().value
    val loading = cartViewModel.loading.collectAsState().value
    val error = cartViewModel.error.collectAsState().value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                error != null -> Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
                storeResults.isEmpty() -> Text(
                    text = stringResource(R.string.no_stores_available),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(storeResults) { storeWithTotals ->
                        StoreItem(
                            storeWithTotals = storeWithTotals,
                            onClick = { /* TODO: Navigate to checkout */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StoreItem(storeWithTotals: StoreWithTotals, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(storeWithTotals.store.imagePath ?: ""),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = storeWithTotals.store.storeName,
                    style = MaterialTheme.typography.titleMedium)
                Text(
                    text = storeWithTotals.store.storeLocation,
                    style = MaterialTheme.typography.bodyMedium)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${"%.2f".format(storeWithTotals.totalPrice)}",
                    style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${storeWithTotals.totalItems} ${stringResource(R.string.items)}",
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}