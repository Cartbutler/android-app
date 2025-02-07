package com.example.cartbutler.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cartbutler.R

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
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header text for the categories section
        Text(
            text = stringResource(id = R.string.categories_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        // Arrange categories two per row
        for (i in categories.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CategoryItem(categories[i]) {
                    println("Navigating to ${categories[i]}")
                }
                if (i + 1 < categories.size) {
                    CategoryItem(categories[i + 1]) {
                        println("Navigating to ${categories[i + 1]}")
                    }
                }
            }
        }
    }
}
