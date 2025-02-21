package com.example.cartbutler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.ApiService
import com.example.cartbutler.network.Product
import kotlinx.coroutines.Job
import com.example.cartbutler.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryProductsViewModel(
    private val apiService: ApiService = RetrofitInstance.api
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _error

    private var currentJob: Job? = null

    fun loadProductsByCategory(categoryId: Int) {
        if (_isLoading.value) return

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = apiService.searchProducts(
                    query = null,
                    categoryID = categoryId
                )
                _products.value = response
            } catch (e: Exception) {
                _error.value = "Error: ${e.message ?: "Unknown error"}"
                _products.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}