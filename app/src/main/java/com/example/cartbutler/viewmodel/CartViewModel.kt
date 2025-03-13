package com.example.cartbutler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartbutler.repositories.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    private val _cartItemsCount = MutableStateFlow(0)
    val cartItemsCount: StateFlow<Int> = _cartItemsCount.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun addToCart(productId: Int, quantity: Int = 1) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _cartItemsCount.value += quantity
                repository.addToCart(productId, quantity)
                val updatedCart = repository.getCart()
                _cartItemsCount.value = updatedCart.cartItems.sumOf { it.quantity }
            } catch (e: Exception) {
                _cartItemsCount.value -= quantity
                _error.value = "Error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadCartCount() {
        viewModelScope.launch {
            try {
                val cart = repository.getCart()
                _cartItemsCount.value = cart.cartItems.sumOf { it.quantity }
            } catch (e: Exception) {
                _error.value = "Error loading cart: ${e.message}"
            }
        }
    }
}