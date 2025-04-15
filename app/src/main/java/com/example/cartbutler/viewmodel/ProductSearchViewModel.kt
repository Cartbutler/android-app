package com.example.cartbutler.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.ApiService
import com.example.cartbutler.network.LocaleHelper
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
    private var lastCategoryName: String? = null

    fun loadProductsByQuery(query: String, context : Context) {
        lastQuery = query
        lastCategoryId = null
        lastCategoryName = null

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _searchQuery.value = null
            _categoryName.value = null
            _products.value = emptyList()

            try {
                val languageId = LocaleHelper.currentLanguage(context)
                val response = apiService.searchProducts(query, null, languageId = languageId)
                _products.value = response
                _searchQuery.value = query
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadProductsByCategory(categoryId: Int, categoryName: String, context: Context) {
        lastCategoryId = categoryId
        lastCategoryName = categoryName
        lastQuery = null

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _searchQuery.value = null
            _products.value = emptyList()

            try {
                val languageId = LocaleHelper.currentLanguage(context)
                val response = apiService.searchProducts(null, categoryId, languageId = languageId)
                _products.value = response
                _categoryName.value = categoryName
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retry(context: Context) {
        when {
            lastQuery != null -> loadProductsByQuery(lastQuery!!, context)
            lastCategoryId != null && lastCategoryName != null ->
                loadProductsByCategory(lastCategoryId!!, lastCategoryName!!, context)
        }
    }
}