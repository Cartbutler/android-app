package com.example.cartbutler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.ApiService
import com.example.cartbutler.network.RetrofitInstance
import com.example.cartbutler.network.networkModels.ProductSuggestion
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductSuggestionViewModel : ViewModel() {
    private val _suggestions = MutableStateFlow<List<ProductSuggestion>>(emptyList())
    val suggestions = _suggestions.asStateFlow()
    private val apiService = RetrofitInstance.api
    private var searchJob: Job? = null

    fun fetchSuggestions(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            if (query.isNotEmpty()) {
                try {
                    _suggestions.value = apiService.getSuggestions(query)
                } catch (e: Exception) {
                    _suggestions.value = emptyList()
                }
            } else {
                _suggestions.value = emptyList()
            }
        }
    }
}