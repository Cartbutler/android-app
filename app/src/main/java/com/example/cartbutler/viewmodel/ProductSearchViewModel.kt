package com.example.cartbutler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.ApiService
import com.example.cartbutler.network.networkModels.Product
import com.example.cartbutler.network.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductSearchViewModel(
    private val apiService: ApiService = RetrofitInstance.api
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _searchQuery = MutableStateFlow<String?>(null)
    val searchQuery: StateFlow<String?> = _searchQuery

    private val _categoryName = MutableStateFlow<String?>(null)
    val categoryName: StateFlow<String?> = _categoryName

    private var currentJob: Job? = null
    private var lastQuery: String? = null
    private var lastCategoryId: Int? = null

    fun loadProducts(query: String?, categoryId: Int?) {
        lastQuery = query
        lastCategoryId = categoryId

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _searchQuery.value = null
            _categoryName.value = null
            _products.value = emptyList()

            try {
                val response = apiService.searchProducts(query, categoryId)
                _products.value = response

                query?.let { _searchQuery.value = it }

            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retry() {
        loadProducts(lastQuery, lastCategoryId)
    }

    fun setCategoryName(name: String?) {
        _categoryName.value = name
    }
}