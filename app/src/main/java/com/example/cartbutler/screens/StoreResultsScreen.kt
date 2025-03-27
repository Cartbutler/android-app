package com.example.cartbutler.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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

    val bestDeal = storeResults.minByOrNull { it.totalPrice }
    val otherStores = storeResults.filterNot { it == bestDeal }

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
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.popBackStack() }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(8.dp))
                        }
                    }

                    bestDeal?.let {
                        item {
                            Text(
                                text = stringResource(R.string.best_deal),
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        item {
                            StoreItem(
                                storeWithTotals = it,
                                onClick = { /* TODO: Navigate to checkout */ },
                                isBestDeal = true
                            )
                        }
                    }

                    if (otherStores.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.other_stores),
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 16.dp
                                )
                            )
                        }
                        items(otherStores) { store ->
                            StoreItem(
                                storeWithTotals = store,
                                onClick = { /* TODO: Navigate to checkout */ },
                                isBestDeal = false
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StoreItem(storeWithTotals: StoreWithTotals, onClick: () -> Unit, isBestDeal: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isBestDeal) 8.dp else 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isBestDeal) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(storeWithTotals.store.imagePath ?: ""),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = storeWithTotals.store.storeName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                    Text(
                        text = storeWithTotals.store.storeLocation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${"%.2f".format(storeWithTotals.totalPrice)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Text(
                    text = "${storeWithTotals.totalItems} ${stringResource(R.string.items)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
    }
}