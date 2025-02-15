package com.example.cartbutler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.Product
import com.example.cartbutler.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductSearchViewModel : ViewModel() {
    private val apiService = RetrofitInstance.api
    private var lastQuery: String = ""

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun searchProducts(query: String) {
        lastQuery = query
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // force error for testing
                //throw Exception("Simulated error")
                _searchResults.value = apiService.searchProducts(query = query, categoryID = null)
            } catch (e: Exception) {
                _error.value = "Failed to load results. Please try again."
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retrySearch() {
        if (lastQuery.isNotBlank()) {
            searchProducts(lastQuery)
        }
    }
}