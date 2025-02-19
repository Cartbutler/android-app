package com.example.cartbutler.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.cartbutler.R
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults

/**
 * Displays an OutlinedTextField with an auto-suggest dropdown.
 *
 * When the user types, a list of suggestions is shown. The colors for borders and background are now
 * based on the current MaterialTheme.colorScheme (which uses your custom palette).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchWithDropdown(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    filteredProducts: List<String>,
    onSuggestionSelected: (String) -> Unit,
    onSearchConfirmed: (String) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                onSearchQueryChange(it)
                isDropdownExpanded = it.isNotEmpty()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(50.dp),
            placeholder = { Text(stringResource(R.string.search)) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        onSearchQueryChange("")
                        isDropdownExpanded = false
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear text")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchConfirmed(searchQuery)
                    isDropdownExpanded = false
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.secondary,
                    backgroundColor = MaterialTheme.colorScheme.secondary
                )
            )
        )

        if (isDropdownExpanded && filteredProducts.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column {
                    filteredProducts.forEach { suggestion ->
                        Text(
                            text = suggestion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    onSuggestionSelected(suggestion)
                                    isDropdownExpanded = false
                                    focusRequester.requestFocus()
                                },
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}