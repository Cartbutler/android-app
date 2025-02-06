package com.example.cartbutler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cartbutler.ui.theme.CartbutlerTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CartbutlerTheme {
                var searchQuery by remember { mutableStateOf("") }
                val allProducts =
                    listOf("Apple", "Banana", "Carrot", "Milk", "Bread", "Cheese", "Coca-Cola", "Pizza")

                val filteredProducts = remember(searchQuery) {
                    if (searchQuery.isEmpty()) emptyList()
                    else allProducts.filter { it.contains(searchQuery, ignoreCase = true) }.sorted()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            SearchWithDropdown(
                                searchQuery = searchQuery,
                                onSearchQueryChange = { searchQuery = it },
                                filteredProducts = filteredProducts,
                                onProductSelected = { searchQuery = it }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            CategorySection()
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchWithDropdown(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    filteredProducts: List<String>,
    onProductSelected: (String) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                onSearchQueryChange(it)
                isDropdownExpanded = it.isNotEmpty()  // Keeps the suggestion list open if there is text
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(50.dp),
            placeholder = { Text(stringResource(R.string.search)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        onSearchQueryChange("")  // Clears the text
                        isDropdownExpanded = false // Hides the suggestions
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear text")
                    }
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.LightGray,
                containerColor = Color.White
            )
        )

        // Display suggestions below the search field WITHOUT interfering with input
        if (isDropdownExpanded && filteredProducts.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Column {
                    filteredProducts.forEach { product ->
                        Text(
                            text = product,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    onProductSelected(product) // Updates the field with the selection
                                    isDropdownExpanded = false // Closes the list
                                    focusRequester.requestFocus() // Keeps focus on input
                                },
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySection() {
    val categories = listOf("Vegetables", "Fruits", "Seafood", "Dairy", "Meat", "Beverages", "Snacks", "Bakery")

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.categories_label),
            style = MaterialTheme.typography.bodyMedium
        )
        for (i in categories.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CategoryItem(categories[i]) { println("Navigating to ${categories[i]}") }
                if (i + 1 < categories.size) {
                    CategoryItem(categories[i + 1]) { println("Navigating to ${categories[i + 1]}") }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(name: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(180.dp)
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(text = name, fontSize = 16.sp)
            }
        }
    }
}