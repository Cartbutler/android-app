package com.example.cartbutler.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cartbutler.network.ApiService
import com.example.cartbutler.viewmodel.ProductSuggestionViewModel

@Suppress("UNCHECKED_CAST")
class ProductSuggestionViewModelFactory(
    private val apiService: ApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductSuggestionViewModel::class.java)) {
            return ProductSuggestionViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
