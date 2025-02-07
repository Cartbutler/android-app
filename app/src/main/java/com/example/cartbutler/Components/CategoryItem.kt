package com.example.cartbutler.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cartbutler.ui.theme.BorderDark
import com.example.cartbutler.ui.theme.BorderLight

/**
 * Displays a single category item as a Card with an outline (border).
 *
 * The border is added to emphasize the category. Its color is selected based on the current theme.
 *
 * @param name The category name to display.
 * @param onClick The callback invoked when the item is clicked.
 */
@Composable
fun CategoryItem(name: String, onClick: () -> Unit) {
    val borderColor: Color = if (isSystemInDarkTheme()) BorderDark else BorderLight

    Box(
        modifier = Modifier
            .size(180.dp)
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxSize()
                .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
