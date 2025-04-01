package com.example.cartbutler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.LocaleHelper
import com.example.cartbutler.network.networkModels.Product
import com.example.cartbutler.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context

class ProductDetailViewModel : ViewModel() {
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _error

    private val apiService = RetrofitInstance.api

    fun loadProduct(productId: Int, context: Context) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val languageId = LocaleHelper.currentLanguage(context)
                val response = apiService.getProductById(
                    productId = productId,
                    languageId = languageId
                )
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