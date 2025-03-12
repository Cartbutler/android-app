package com.example.cartbutler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.repositories.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.cartbutler.network.networkModels.CartResponse

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    private val _cartState = MutableStateFlow<CartResponse?>(null)
    val cartState: StateFlow<CartResponse?> = _cartState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchCart() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _cartState.value = repository.getCart()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}