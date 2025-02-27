package com.example.cartbutler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.networkModels.Product
import com.example.cartbutler.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val apiService: com.example.cartbutler.network.ApiService = RetrofitInstance.api
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _error

    fun loadProduct(productId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getProductById(productId)
                _product.value = response
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
