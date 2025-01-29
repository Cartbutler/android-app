package com.example.cartbutler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        var isSearchActive by remember { mutableStateOf(false) }
                        var searchQuery by remember { mutableStateOf("") }

                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            active = isSearchActive,
                            onActiveChange = { isSearchActive = it },
                            onSearch = { println("User searched: $searchQuery") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 19.dp)
                        ) {
                            Text(text = "Type your search here...")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        CategorySection()
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
        Text(text = "Categories", fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp))
        for (i in categories.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
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
            .size(200.dp)
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

@Preview(showBackground = true)
@Composable
fun PreviewCategorySection() {
    CartbutlerTheme {
        CategorySection()
    }
}
