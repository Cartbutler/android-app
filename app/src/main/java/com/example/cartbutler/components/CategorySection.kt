package com.example.cartbutler.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cartbutler.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*

/**
 * Displays a section of categories arranged in rows.
 *
 * The header text uses MaterialTheme.colorScheme.primary, and the items (provided by CategoryItem)
 * use the colors defined in the theme.
 */
@Composable
fun CategorySection() {
    // List of categories
    val categories = listOf("Vegetables", "Fruits", "Seafood", "Dairy", "Meat", "Beverages", "Snacks", "Bakery")

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header text for the categories section
        Text(
            text = stringResource(id = R.string.categories_label),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Apply 2 items per row using LazyVerticalGrid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryItem(category) {
                    println("Navigating to $category")
                }
            }
        }
    }
}
