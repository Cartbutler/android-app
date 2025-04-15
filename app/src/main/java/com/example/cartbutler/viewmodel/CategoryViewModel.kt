package com.example.cartbutler.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.network.LocaleHelper
import com.example.cartbutler.network.RetrofitInstance
import com.example.cartbutler.network.networkModels.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    fun loadCategories(context: Context) {
        viewModelScope.launch {
            try {
                val languageId = LocaleHelper.currentLanguage(context)
                _categories.value = RetrofitInstance.api.getCategories(languageId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refresh(context: Context) {
        loadCategories(context)
    }
}