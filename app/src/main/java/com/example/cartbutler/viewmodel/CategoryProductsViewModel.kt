package com.example.cartbutler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.ApiService
import com.example.cartbutler.network.Product
import com.example.cartbutler.network.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryProductsViewModel(
    private val apiService: ApiService = RetrofitInstance.api
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _categoryName = MutableStateFlow<String?>(null)
    val categoryName: StateFlow<String?> = _categoryName

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private var currentJob: Job? = null

    fun loadCategoryAndProducts(categoryId: Int) {
        if (_isLoading.value) return

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _categoryName.value = null
            _products.value = emptyList()

            try {
                val response = apiService.searchProducts(
                    query = null,
                    categoryID = categoryId
                )

                _products.value = response
                _categoryName.value = response.firstOrNull()?.categoryName
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}