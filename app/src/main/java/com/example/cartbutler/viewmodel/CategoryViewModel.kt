package com.example.cartbutler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.Category
import com.example.cartbutler.network.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class CategoryViewModel : ViewModel() {
    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                categories = RetrofitInstance.api.getCategories()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}